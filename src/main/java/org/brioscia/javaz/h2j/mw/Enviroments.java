package org.brioscia.javaz.h2j.mw;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.brioscia.javaz.expression.Executor;
import org.brioscia.javaz.expression.InvokerException;
import org.brioscia.javaz.expression.LexicalParser;
import org.brioscia.javaz.expression.NodeToken;
import org.brioscia.javaz.expression.SyntaxError;
import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.filter.cast.Shape;
import org.brioscia.javaz.h2j.fs.VirtualFileSystem;

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
	public static boolean enableCacheFile = true;
	public static boolean trace = false;	
	public static String ROOT_VAR = "#{ROOT}";
	public static OnResponseErrorCallback errorCallback;
	
	// non è l'init del bean è invocato durante l'inizializzazione del filtro
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
	
	public Enviroments() {
		this.push("ROOT", Enviroments.contextRoot);
	}

	public String getStringValue(String fullname) throws H2JFilterException {
		Object o;
		o = this.getObject(fullname);
		return o != null ? Shape.toHTML(o) : "";
	}

	public Object getObject(String fullname) throws H2JFilterException {
		NodeToken node;

		try {
			node = LexicalParser.process(fullname);
			return Executor.get(node, this);
		} catch (SyntaxError | InvokerException e) {
			throw new H2JFilterException(fullname, e);
		}
	}

	public Object setBean(String fullName, Object[] value) throws H2JFilterException {
		NodeToken node;
		try {
			node = LexicalParser.process(fullName);
			return Executor.set(node, this, value);
		} catch (SyntaxError | InvokerException e) {
			throw new H2JFilterException(fullName, e);
		}
	}

	public String eval(String elname) throws H2JFilterException {
		int posStart;
		int posEnd;

		while (true) {
			posStart = elname.indexOf("#{");
			if (posStart != -1) {
				posEnd = elname.indexOf("}", posStart);
				if (posEnd != -1) {
					String exp;
					String value;
					String fullExp;

					fullExp = elname.substring(posStart, posEnd + 1);
					exp = elname.substring(posStart + 2, posEnd);
					value = this.getStringValue(exp);

					elname = elname.replace(fullExp, value);
					continue;
				}
			}
			break;
		}

		return elname;
	}

	/**
	 * 
	 * prepara l'espressione da chiamare alla submit da html valuta solo gli
	 * elementi dell'ambiente locale
	 * 
	 * @param elname stringa da valutare
	 * @return stringa html
	 * @throws H2JFilterException sollevata se la stringa è malformata o non è possibile valutarla
	 */
	public String evalForHTMLCall(String elname) throws H2JFilterException {
		int posStart;
		int posEnd;
		NodeToken node;

		while (true) {
			posStart = elname.indexOf("#{");
			if (posStart != -1) {
				posEnd = elname.indexOf("}", posStart);
				if (posEnd != -1) {
					String exp;
					String value;
					String fullExp;

					fullExp = elname.substring(posStart, posEnd + 1);
					exp = elname.substring(posStart + 2, posEnd);

					try {
						Object object;
						node = LexicalParser.process(exp);
						this.disableCDIContext();
						object= Executor.get(node, this);
						assertNotNull(object, "object " + node.getValue() + " is null");
						value = object.toString();
					} catch (SyntaxError | InvokerException e) {
						throw new H2JFilterException(elname, e);
					} finally {
						this.enableCDIContext();
					}

					elname = elname.replace(fullExp, value);
					continue;
				}
			}
			break;
		}

		return elname;
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
	
	/**
	 * verifica se trattasi di una espressione da valutare
	 * 
	 * @param elname stringa da verificare
	 * @return true se è una espressione
	 */
	public static boolean isELName(String elname) {
		return (elname!=null) && (elname.startsWith("#{")) && (elname.endsWith("}"));
	}
}
