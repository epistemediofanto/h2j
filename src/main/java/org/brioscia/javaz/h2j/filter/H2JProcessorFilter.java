package org.brioscia.javaz.h2j.filter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;

import org.brioscia.javaz.expression.InvokerException;
import org.brioscia.javaz.h2j.filter.cast.Converter;
import org.brioscia.javaz.h2j.filter.cast.Shape;
import org.brioscia.javaz.h2j.mw.Enviroments;
import org.brioscia.javaz.h2j.mw.HtmlBindName.StoreObject;
import org.brioscia.javaz.h2j.mw.Trasnslator;
import org.brioscia.javaz.h2j.mw.XmlProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebFilter(urlPatterns = "/*", initParams = @WebInitParam(name = "fileTypes", value = "xhtml"))
public class H2JProcessorFilter implements Filter {

	public static final String LOGNAME = "h2j";
	public static String EXT = ".xhtml";
	public static String RMI = ".rmi";
	public static String CALL_STRING_EXT = RMI + EXT;
	public static String JSON_RESPONSE_ID = "_json";

	@Inject
	private DialogueBoost dialogueBoost;

	@Inject
	private H2JContext h2jContext;

	public void init(FilterConfig fConfig) throws ServletException {
		String ext;
		try {
			Shape.initCommonConverter();
			Enviroments.init(fConfig.getServletContext());
			Trasnslator.init();

			ext = fConfig.getServletContext().getInitParameter("h2j.fileTypes");
			if (ext != null) {
				EXT = "." + ext;
				CALL_STRING_EXT = RMI + EXT;
			}
			Logger.getGlobal().log(Level.INFO, "h2j page type: " + EXT);
			Logger.getGlobal().log(Level.INFO, "H2J: *** h2j processor started. *** " + this.getVersion());

		} catch (H2JFilterException e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private String getVersion() {
		Package aPackage = this.getClass().getPackage();
		String implementationVersion = aPackage.getImplementationVersion();
		String implementationVendor = aPackage.getImplementationVendor();
		return implementationVersion + " : " + implementationVendor;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest servletRequest;
			Enviroments enviroments;

			servletRequest = (HttpServletRequest) request;

			String page = servletRequest.getRequestURI();

			if (page.endsWith(EXT)) {
				String contextRoot;
				boolean isJsonResponse;
				try {
					enviroments = (Enviroments) Enviroments.getCDIObject("env");
					enviroments.resetMode(); // reimposta la modalità array, previene eventuali bug
					this.h2jContext.set(this, enviroments, request, response);

					contextRoot = Enviroments.getServletContext().getContextPath();
					page = page.substring(contextRoot.length());

					try {
						isJsonResponse = this.processRequest(enviroments, servletRequest, response);
					} catch (H2JFilterException e) {
						throw new H2JFilterException("On request: " + page, e);
					}

					page = URLDecoder.decode(page, "utf-8");

					int n;
					char c;
					int lastPos = -1;
					String path;
					String name;

					n = page.length();
					for (int i = 0; i < n; ++i) {
						c = page.charAt(i);
						if (c == '/') {
							lastPos = i;
						} else if (c == '(') {
							break;
						}
					}

					path = page.substring(0, lastPos + 1);
					if (page.endsWith(CALL_STRING_EXT)) {
						name = page.substring(lastPos + 1, n - H2JProcessorFilter.CALL_STRING_EXT.length());
					} else {
						name = page.substring(lastPos + 1, n - H2JProcessorFilter.EXT.length());
					}

					StoreObject storeObject = enviroments.getObjectFromNameStore(name);
					if (isJsonResponse) {
						try {
							this.processJSonResponse(enviroments, path, name, servletRequest, response);
						} catch (H2JFilterException e) {
							throw new H2JFilterException("On json response: " + page, e);
						}
					} else {
						if (storeObject.type == Enviroments.DYNAMIC_CALL) {
							page = path + enviroments.getStringValue(storeObject.name);
						}
						if (!this.h2jContext.isRefresh()) {
							enviroments.clearBindName();
							this.h2jContext.setRefresh(false);
						}
						try {
							this.processResponse(enviroments, page, servletRequest, response);
						} catch (H2JFilterException e) {
							throw new H2JFilterException("On response: " + page, e);
						}
					}

				} catch (InvokerException | H2JFilterException e) {
					Logger.getGlobal().log(Level.SEVERE, "on response:" + page + ": " + e.getMessage(), e);
					e.printStackTrace();
				}
				return;
			}
		}

		chain.doFilter(request, response);

	}

	private void processJSonResponse(Enviroments enviroments, String path, String name, HttpServletRequest request,
			ServletResponse response) throws H2JFilterException {
		Object object;
		String jsonString;
		byte[] byteString;
		ObjectMapper mapper;

		object = enviroments.getObject(name);
		mapper = new ObjectMapper();

		try {
			jsonString = mapper.writeValueAsString(object);
			byteString = jsonString.getBytes();

			response.setContentType("application/json");
			response.getOutputStream().write(byteString, 0, byteString.length);
			response.flushBuffer();
		} catch (IOException e) {
			throw new H2JFilterException(e);
		}
	}

	boolean processRequest(Enviroments enviroments, HttpServletRequest request, ServletResponse response)
			throws H2JFilterException {

		Enumeration<String> eList;
		String pName;
		String[] values;
		StoreObject storeObject;
		Converter converter;
		boolean jsonResponse = false;
		Object[] o;

		eList = request.getParameterNames();
		while (eList.hasMoreElements()) {
			pName = eList.nextElement();
			values = request.getParameterValues(pName);
			if ((values == null) || (values.length == 0)) {
				continue;
			}

			if (JSON_RESPONSE_ID.equals(pName)) {
				jsonResponse = "true".equals(values[0]);
			}

			storeObject = enviroments.getObjectFromNameStore(pName);
			pName = storeObject.name;
			converter = storeObject.converter;

			o = new Object[values.length];
			for (int i = 0; i < values.length; ++i) {
				if (converter != null) {
					o[i] = converter.fromString(values[i]);
				} else {
					o[i] = values[i];
				}
			}
			if (Enviroments.trace) {
				System.out.println("set: " + pName + " = " + o);
			}

			enviroments.setBean(pName, o);

		}
		return jsonResponse;
	}

	void processResponse(Enviroments enviroments, String page, HttpServletRequest request, ServletResponse response)
			throws H2JFilterException {
		XmlProcessor xmlProcessor;
		InputStream is;

		if (!this.h2jContext.isDirectResponse()) {

			is = Enviroments.getServletContext().getResourceAsStream(page);
			if (is == null) {
				throw new H2JFilterException("page not found: " + page);
			}
			try {

				response.setContentType("text/html");

				if (page.endsWith(EXT)) {
					xmlProcessor = new XmlProcessor(enviroments, page);
					xmlProcessor.process(is, response.getOutputStream());
				} else {
					byte[] buffer;
					int size = 4096;
					int read;
					int offset = 0;

					buffer = new byte[size];
					while ((read = is.read(buffer, 0, size)) != -1) {
						response.getOutputStream().write(buffer, offset, read);
						offset += read;
					}
				}

				is.close();

			} catch (IOException e) {
				throw new H2JFilterException(e);
			}
		}
		this.dialogueBoost.cancel();
	}

}
