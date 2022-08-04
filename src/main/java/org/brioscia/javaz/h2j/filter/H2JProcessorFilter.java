package org.brioscia.javaz.h2j.filter;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.Enumeration;

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
import javax.xml.parsers.ParserConfigurationException;

import org.brioscia.javaz.expression.InvokerException;
import org.brioscia.javaz.h2j.filter.cast.Converter;
import org.brioscia.javaz.h2j.filter.cast.Shape;
import org.brioscia.javaz.h2j.mw.Enviroments;
import org.brioscia.javaz.h2j.mw.H2JLog;
import org.brioscia.javaz.h2j.mw.HtmlBindName.StoreObject;
import org.brioscia.javaz.h2j.mw.Trasnslator;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebFilter(urlPatterns = "/*", initParams = @WebInitParam(name = "fileTypes", value = "xhtml"))
public class H2JProcessorFilter implements Filter, Serializable {

	private static final long serialVersionUID = 1L;
	public static final String LOGNAME = "h2j";
	public static String EXT = ".xhtml";
	public static String RMI = ".rmi";
	public static String CALL_STRING_EXT = RMI + EXT;
	public static String JSON_RESPONSE_ID = "_json";
	public static String REQUEST_ECODE = "UTF-8";
	public static String XHTML_ECODE = "UTF-8";
	public static String XHTML_INDENT = "no";
	public static String DATE_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // formato per DateConverter
	private static final String CONTENT_TYPE="text/html";//"application/xhtml+xml"; // text/html

	@Inject
	private DialogueBoost dialogueBoost;

	@Inject
	private H2JContext h2jContext;

	private boolean disableErrorHandler = false;

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
			H2JLog.info("h2j page type: " + EXT);
			H2JLog.info("H2J: *** h2j processor started. *** " + this.getVersion());

		} catch (H2JFilterException e) {
			H2JLog.error(e.getMessage(), e);
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
		boolean doChain = true;

		if (request instanceof HttpServletRequest) {
			HttpServletRequest servletRequest;
			Enviroments enviroments = null;

			servletRequest = (HttpServletRequest) request;

			String page = servletRequest.getRequestURI();
			doChain = !page.endsWith(EXT);
			if (!doChain) {
				String contextRoot;
				boolean isJsonResponse;
				try {
					enviroments = (Enviroments) Enviroments.getCDIObject("env");
				} catch (InvokerException e) {
					H2JLog.error(e, "critical error in get CDI enviroments: %s", e.getMessage());
					return;
				}
				try {
					this.h2jContext.set(this, enviroments, request, response);

					contextRoot = Enviroments.getServletContext().getContextPath();
					page = page.substring(contextRoot.length());

					try {
						isJsonResponse = this.processRequest(enviroments, servletRequest, response);
					} catch (H2JFilterException e) {
						throw new H2JFilterException("On request: " + page, e);
					}

					page = URLDecoder.decode(page, REQUEST_ECODE);

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
							if (storeObject.type == Enviroments.DYNAMIC_CALL) {
								name = storeObject.name;
							}
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

						if (!this.h2jContext.isDirectResponse()) {
							this.processResponse(enviroments, page, servletRequest, response);
						} else {
							doChain = true;
						}
					}

				} catch (H2JFilterException e) {
					this.handleErrorPage(enviroments, request, response, page, e);
				}
				return;
			}
		}

		if (doChain) {
			chain.doFilter(request, response);
		}
	}

	private void handleErrorPage(Enviroments enviroments, ServletRequest request, ServletResponse response, String page,
			Exception e) {
		if (enviroments.getErrorCallback() != null) {
			H2JLog.error(e, "error in page %s, exception %s", page, e.getMessage());
			if (this.disableErrorHandler) {
				H2JLog.error(e, "critical error, loop in page error callback, on page %s - %s", page, e.getMessage());
			} else {
				this.disableErrorHandler = true;
				enviroments.getErrorCallback().onResponseError(this, request, response, e);
			}
		} else {
			H2JLog.error(e, "error on response page %s - %s ", page, e.getMessage());
		}
		this.disableErrorHandler = false;
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

			if (pName.indexOf('.') != -1) {
				H2JLog.trace("set: %s = %s", pName, o);
				enviroments.setBean(pName, o);
			}
		}
		return jsonResponse;
	}

	public void processResponse(String page, HttpServletRequest request, ServletResponse response)
			throws H2JFilterException {
		Enviroments enviroments;
		try {
			enviroments = (Enviroments) Enviroments.getCDIObject("env");
			this.processResponse(enviroments, page, request, response);
		} catch (InvokerException e) {
			throw new H2JFilterException(e);
		}
	}

	protected void processResponse(Enviroments enviroments, String page, HttpServletRequest request,
			ServletResponse response) throws H2JFilterException {
		XmlProcessor xmlProcessor;
		Document docXHTML;

		try {
			docXHTML = Enviroments.getFileSystem().loadDocument(page);

			response.setContentType(CONTENT_TYPE);

			response.getOutputStream().write("<!DOCTYPE html>".getBytes());
			
			xmlProcessor = new XmlProcessor(enviroments, page);
			xmlProcessor.process(docXHTML, response.getOutputStream());

		} catch (IOException e) {
			throw new H2JFilterException(e);
		}

		this.dialogueBoost.cancel();
	}

//	private static String toString(Object o) {
//		if (o == null)
//			return "null";
//		if (o instanceof Object[]) {
//			Object[] a = (Object[]) o;
//			String out = "[";
//			for (Object oj : a) {
//				out += (oj != null ? oj : "null") + ",";
//			}
//			out += "]";
//			return out;
//		}
//		return o.toString();
//	}
}
