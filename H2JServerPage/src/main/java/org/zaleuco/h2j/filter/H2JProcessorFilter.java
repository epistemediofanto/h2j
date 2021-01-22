package org.zaleuco.h2j.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import org.zaleuco.h2j.mw.Enviroments;
import org.zaleuco.h2j.mw.Trasnslator;
import org.zaleuco.h2j.mw.XmlProcessor;

@WebFilter(filterName = "h2jfilter2", urlPatterns = "/*", initParams = @WebInitParam(name = "fileTypes", value = "h2j"))
public class H2JProcessorFilter implements Filter {

	public static final String EXT = ".h2j";
	public static final String CALL_STRING_EXT = ".call" + EXT;

	private static final Logger Log = Logger.getLogger(H2JProcessorFilter.class.getName());

	public void init(FilterConfig fConfig) throws ServletException { 
		try {
			Enviroments.init(fConfig.getServletContext());
			Trasnslator.init();
		} catch (H2JFilterException e) {
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage(), e);
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
						this.processResponse(enviroments, page, servletRequest, response, chain);
					}
				} catch (H2JFilterException e) {
					e.printStackTrace();
					Log.log(Level.SEVERE, e.getMessage(), e);
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
			pos = page.lastIndexOf('/') + 1;
			prefixPage = page.substring(0, pos);
			envName = page.substring(pos);
			envName = envName.substring(0, envName.length() - CALL_STRING_EXT.length());

			envName = URLDecoder.decode(envName, StandardCharsets.UTF_8.toString());
//			objName = Enviroments.getCDIObject(envName).toString();
			objName = enviroments.getValue(envName);
			newPage = prefixPage + objName;

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

		eList = request.getParameterNames();
		while (eList.hasMoreElements()) {
			pName = eList.nextElement();
			value = request.getParameter(pName);
			enviroments.setBean(pName, value);
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

}
