package org.zaleuco.h2j.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.System.Logger.Level;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

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

@WebFilter(filterName = "org.zaleuco.h2j.filter", urlPatterns = "/*", initParams = @WebInitParam(name = "fileTypes", value = "h2j"))
public class H2JProcessorFilter implements Filter {

	public static final String LOGNAME = "h2j";
	public static String EXT = ".h2j";
	public static String CALL_STRING_EXT = ".call" + EXT;

	public void init(FilterConfig fConfig) throws ServletException {
		String ext;
		try {
			Shape.initCommonConverter();
			Enviroments.init(fConfig.getServletContext());
			Trasnslator.init();

			ext = fConfig.getServletContext().getInitParameter("h2j.fileTypes");
			if (ext != null) {
				EXT = "." + ext;
				CALL_STRING_EXT = ".call" + EXT;
			}
			System.getLogger(LOGNAME).log(Level.INFO, "h2j page type: " + EXT);
			System.getLogger(LOGNAME).log(Level.INFO, "H2J: *** h2j processor started. ***");

		} catch (H2JFilterException e) {
			System.getLogger(LOGNAME).log(Level.ERROR, e.getMessage(), e);
			throw new RuntimeException(e);
		}
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

				try {

					enviroments = (Enviroments) Enviroments.getCDIObject("env");

					contextRoot = Enviroments.getServletContext().getContextPath();
					page = page.substring(contextRoot.length());

					this.processRequest(enviroments, servletRequest, response, chain);

					if (page.endsWith(CALL_STRING_EXT)) {
						this.processCall(enviroments, page, servletRequest, response, chain);
					} else {
						enviroments.clearBindName();
						this.processResponse(enviroments, page, servletRequest, response, chain);
					}
				} catch (InvokerException | H2JFilterException e) {
					System.getLogger(LOGNAME).log(Level.ERROR, e.getMessage(), e);
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

	private void processCall(Enviroments enviroments, String page, HttpServletRequest request, ServletResponse response,
			FilterChain chain) throws H2JFilterException {
		String envName;
		String objName;
		String newPage;
		String prefixPage;
		int pos;
		try {
			pos = lastPos(page, '/') + 1;
			prefixPage = page.substring(0, pos);
			envName = page.substring(pos);
			envName = envName.substring(0, envName.length() - CALL_STRING_EXT.length());
			envName = URLDecoder.decode(envName, StandardCharsets.UTF_8.toString());
			// TODO
			envName = enviroments.originalName(envName).name;
//			objName = Enviroments.getCDIObject(envName).toString();
			objName = enviroments.getStringValue(envName);
			newPage = prefixPage + objName;

			enviroments.clearBindName();

			this.processResponse(enviroments, newPage, request, response, chain);
		} catch (UnsupportedEncodingException e) {
			throw new H2JFilterException(e);
		}
	}

	private void processRequest(Enviroments enviroments, HttpServletRequest request, ServletResponse response,
			FilterChain chain) throws H2JFilterException {

		Enumeration<String> eList;
		String pName;
		String value;
		StoreObject storeObject;
		Converter converter;

		eList = request.getParameterNames();
		while (eList.hasMoreElements()) {
			pName = eList.nextElement();
			value = request.getParameter(pName);
			// TODO
			storeObject = enviroments.originalName(pName);
			pName = storeObject.name;
			converter = storeObject.converter;
			if (converter != null) {
				enviroments.setBean(pName, converter.fromString(value));
			} else {
				enviroments.setBean(pName, value);
			}
		}
	}

	private void processResponse(Enviroments enviroments, String page, HttpServletRequest request,
			ServletResponse response, FilterChain chain) throws H2JFilterException {
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

//		InputStream inputStream = application.getResourceAsStream("/META-INF/MANIFEST.MF");
	}

	private static int lastPos(String str, char c) {
		char e;
		int pos = 0;
		int len = str.length();
		for (int i = 0; i < len; ++i) {
			e = str.charAt(i);
			if (e == '/') {
				pos = i;
			} else if (e == '\'') {
				break;
			}
		}
		return pos;
	}

}
