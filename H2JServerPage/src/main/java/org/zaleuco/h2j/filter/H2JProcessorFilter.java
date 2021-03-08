package org.zaleuco.h2j.filter;

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
import javax.servlet.http.HttpServletResponse;

import org.zaleuco.expression.InvokerException;
import org.zaleuco.h2j.filter.cast.Converter;
import org.zaleuco.h2j.filter.cast.Shape;
import org.zaleuco.h2j.mw.Enviroments;
import org.zaleuco.h2j.mw.HtmlBindName.StoreObject;
import org.zaleuco.h2j.mw.Trasnslator;
import org.zaleuco.h2j.mw.XmlProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebFilter(filterName = "org.zaleuco.h2j.filter", urlPatterns = "/*", initParams = @WebInitParam(name = "fileTypes", value = "h2j"))
public class H2JProcessorFilter implements Filter {

	public static final String LOGNAME = "h2j";
	public static String EXT = ".xhtml";
	public static String RMI = ".rmi";
	public static String CALL_STRING_EXT = RMI + EXT;
	public static String JSON_RESPONSE_ID = "_json";	

	@Inject
	private DialogueBoost dialogueBoost;

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
			Logger.getGlobal().log(Level.INFO, "H2J: *** h2j processor started. ***");

		} catch (H2JFilterException e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest servletRequest;
			Enviroments enviroments;

			servletRequest = (HttpServletRequest) request;

			String page = servletRequest.getRequestURI();

			if (page.endsWith(EXT)) {
				String contextRoot;
				boolean jsonResponse;

				try {

					enviroments = (Enviroments) Enviroments.getCDIObject("env");

					contextRoot = Enviroments.getServletContext().getContextPath();
					page = page.substring(contextRoot.length());

					jsonResponse = this.processRequest(enviroments, servletRequest, response, chain);

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
					if (jsonResponse) {
						this.processAjax(enviroments, path, name, servletRequest, response, chain);
					} else {
						if (storeObject.type == Enviroments.DYNAMIC_CALL) {
							page = path + enviroments.getStringValue(storeObject.name);
						} 
						enviroments.clearBindName();
						this.processResponse(enviroments, page, servletRequest, response, chain);
					}

				} catch (InvokerException | H2JFilterException e) {
					Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
					e.printStackTrace();
					this.defaultPage(response);
				}
				return;
			}
		}

		chain.doFilter(request, response);
	}

	@SuppressWarnings("deprecation")
	private void defaultPage(ServletResponse response) {
		((HttpServletResponse) response).setStatus(500, "Contattare l'amministratore.");
	}

	private void processAjax(Enviroments enviroments, String path, String name, HttpServletRequest request, ServletResponse response, FilterChain chain)
			throws H2JFilterException {
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

	private boolean processRequest(Enviroments enviroments, HttpServletRequest request, ServletResponse response, FilterChain chain) throws H2JFilterException {

		Enumeration<String> eList;
		String pName;
		String value;
		StoreObject storeObject;
		Converter converter;
		boolean jsonResponse = false;
		Object o;

		eList = request.getParameterNames();
		while (eList.hasMoreElements()) {
			pName = eList.nextElement();
			value = request.getParameter(pName);

			if (JSON_RESPONSE_ID.equals(pName)) {
				jsonResponse = "true".equals(value);
			}
			
			storeObject = enviroments.getObjectFromNameStore(pName);
			pName = storeObject.name;
			converter = storeObject.converter;
			
			if (converter != null) {
				o = converter.fromString(value); 
			} else {
				o=value;
			}
			if (Enviroments.trace) {
				System.out.println("set: " + pName + " = " + o);
			}
			enviroments.setBean(pName, o);			
		}
		return jsonResponse;
	}

	private void processResponse(Enviroments enviroments, String page, HttpServletRequest request, ServletResponse response, FilterChain chain)
			throws H2JFilterException {
		XmlProcessor xmlProcessor;
		InputStream is;

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

		this.dialogueBoost.cancel();

//		InputStream inputStream = application.getResourceAsStream("/META-INF/MANIFEST.MF");
	}

}
