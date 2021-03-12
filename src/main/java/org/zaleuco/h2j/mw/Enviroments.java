package org.zaleuco.h2j.mw;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.zaleuco.expression.Executor;
import org.zaleuco.expression.InvokerException;
import org.zaleuco.expression.LexicalParser;
import org.zaleuco.expression.NodeToken;
import org.zaleuco.expression.SyntaxError;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.filter.cast.Shape;
import org.zaleuco.h2j.fs.VirtualFileSystem;

@Named("env")
@SessionScoped
public class Enviroments extends Store {

	private static final long serialVersionUID = 1L;
	private static final String CACHE_FILE = "h2j.fileCache";
	private static final String DEVELOPMENT_DOME = "h2j.developmentMode";
	private static final String TRACE = "h2j.trace";

	private static ServletContext servletContext;
	private static String contextRoot;
	private static VirtualFileSystem fileSystem;
	private static boolean enableCacheFile = true;
	public static boolean trace = false;
	
	public static void init(ServletContext context) throws H2JFilterException {
		Enviroments.servletContext = context;
		Enviroments.contextRoot = Enviroments.servletContext.getContextPath();
		Enviroments.fileSystem = new VirtualFileSystem(context);

		Enviroments.enableCacheFile = booleanValue(CACHE_FILE, context.getInitParameter(CACHE_FILE), enableCacheFile);
		Enviroments.developmentMode = booleanValue(DEVELOPMENT_DOME, context.getInitParameter(DEVELOPMENT_DOME),
				developmentMode);
		Enviroments.trace = booleanValue(TRACE, context.getInitParameter(TRACE), trace);

		if (Enviroments.developmentMode) {
			System.out.println("\nH2J: *** WARNING: system is in development mode ***\n");
			Enviroments.enableCacheFile = false;
		}

		if (!Enviroments.enableCacheFile) {
			System.out.println("\nH2J: *** WARNING: cache is disabled ***\n");
		}

	}

	public String getStringValue(String element) throws H2JFilterException {
		Object o;
		o = this.getObject(element);
		return o != null ? Shape.toHTML(o) : "";
	}

	public Object getObject(String fullName) throws H2JFilterException {
		NodeToken node;

		try {
			node = LexicalParser.process(fullName);
			return Executor.get(node, this);
		} catch (SyntaxError | InvokerException e) {
			throw new H2JFilterException(fullName, e);
		}
	}

	public Object setBean(String fullName, Object value) throws H2JFilterException {
		NodeToken node;
		try {
			node = LexicalParser.process(fullName);
			return Executor.set(node, this, value);
		} catch (SyntaxError | InvokerException e) {
			throw new H2JFilterException(fullName, e);
		}
	}

	public String eval(String text) throws H2JFilterException {
		int posStart;
		int posEnd;

		while (true) {
			posStart = text.indexOf("#{");
			if (posStart != -1) {
				posEnd = text.indexOf("}", posStart);
				if (posEnd != -1) {
					String exp;
					String value;
					String fullExp;

					fullExp = text.substring(posStart, posEnd + 1);
					exp = text.substring(posStart + 2, posEnd);
					value = this.getStringValue(exp);

					text = text.replace(fullExp, value);
					continue;
				}
			}
			break;
		}

		return text;
	}

	/**
	 * 
	 * prepara l'espressione da chiamare alla submit da html valuta solo gli
	 * elementi dell'ambiente locale
	 * 
	 * @param fullName stringa da valutare
	 * @return stringa html
	 * @throws H2JFilterException sollevata se la stringa è malformata o non è possibile valutarla
	 */
	public String evalForHTMLCall(String fullName) throws H2JFilterException {
		int posStart;
		int posEnd;
		NodeToken node;

		while (true) {
			posStart = fullName.indexOf("#{");
			if (posStart != -1) {
				posEnd = fullName.indexOf("}", posStart);
				if (posEnd != -1) {
					String exp;
					String value;
					String fullExp;

					fullExp = fullName.substring(posStart, posEnd + 1);
					exp = fullName.substring(posStart + 2, posEnd);

					try {
						node = LexicalParser.process(exp);
						this.disableCDIContext();
						value = Executor.get(node, this).toString();
					} catch (SyntaxError | InvokerException e) {
						throw new H2JFilterException(fullName, e);
					} finally {
						this.enableCDIContext();
					}

					fullName = fullName.replace(fullExp, value);
					continue;
				}
			}
			break;
		}

		return fullName;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static void setServletContext(ServletContext servletContext) {
		Enviroments.servletContext = servletContext;
	}

	public static String getContextRoot() {
		return contextRoot;
	}

	public static VirtualFileSystem getFileSystem() {
		return fileSystem;
	}

	private static boolean booleanValue(String name, String str, boolean def) {
		if (("true".equals(str)) || ("false".equals(str))) {
			return Boolean.parseBoolean(str);
		}
		if (str != null) {
			System.out.println("Invalid setting: " + name + "=" + str + ", use: " + def);
		}
		return def;
	}
}
