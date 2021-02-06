package org.zaleuco.h2j.mw;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.zaleuco.expression.Executor;
import org.zaleuco.expression.InvokerException;
import org.zaleuco.expression.LexicalParser;
import org.zaleuco.expression.NodeToken;
import org.zaleuco.expression.SyntaxError;
import org.zaleuco.h2j.filter.H2JBean;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.fs.VirtualFileSystem;

@Named("env")
@SessionScoped
public class Enviroments extends Store {

	private static final long serialVersionUID = 1L;
	private static final String CACHE_FILE = "h2j.filecache";

	private static ServletContext servletContext;
	private static ComponentCast componentCast;
	private static String contextRoot;
	private static VirtualFileSystem fileSystem;
	private static boolean enableCacheFile = true;	

	public static void init(ServletContext context) throws H2JFilterException {
		Enviroments.servletContext = context;
		Enviroments.componentCast = new ComponentCast();
		Enviroments.contextRoot = Enviroments.servletContext.getContextPath();
		Enviroments.fileSystem = new VirtualFileSystem(context);

		Enviroments.enableCacheFile = booleanValue(CACHE_FILE, context.getInitParameter(CACHE_FILE), true);

		if (!Enviroments.enableCacheFile) {
			System.out.println("\nH2J: *** WARNING: cache system is disabled ***\n");
		}
	}

	public String getStringValue(String element) throws H2JFilterException {
		Object o;
		o = this.getObject(element);
		if (o instanceof H2JBean) {
			return ObjectWrapper.write(o);
		} else {
			return o != null ? o.toString() : "";
		}
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

	public Object setBean(String fullName, String value) throws H2JFilterException {
		try {
			NodeToken node;

			node = LexicalParser.process(fullName);
			return Executor.set(node, this, value);
		} catch (SyntaxError | InvokerException e) {
			throw new H2JFilterException(fullName, e);
		}
	}

//	private Object invoke(String fullName, boolean setter, String value) throws H2JFilterException {
//		Object object;
//		ExpNode node;
//
//		node = ParseFunctionCall.process(fullName);
//		assertNotNull(node, "missing element");
//		assertTrue(node.getType() == Type.Property, "invalid function " + node); // non può essere una funzione
//
//		object = this.invoke(node, setter, value);
//		return object;
//	}
//
//	private Object invoke(ExpNode node, boolean setter, String value) throws H2JFilterException {
//		Object object;
//
//		try {
//
//			object = this.peek(node.getObjectName());
//			if (object == null) {
//				object = getCDIObject(node.getObjectName());
//			}
//			if (object != null) {
//				object = this.getArrayElement(node, object);
//				if (node.getProperty() != null) {
//					object = this.invoke(object, node.getProperty(), setter, value);
//				}
//			} else {
//				assertTrue(node.getParameterList().size() == 0, "invalid function " + node);
//				object = node.getObjectName();
//			}
//
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			throw new H2JFilterException(e);
//		}
//		return object;
//	}
//
//	private Object getArrayElement(ExpNode node, Object o) throws H2JFilterException {
//		int ix;
//		for (int i = 0; i < node.getArrayIndex().size(); ++i) {
//			ix = (int) this.invoke(node.getArrayIndex().get(i), false, null);
//			o = ((Object[]) o)[ix];
//		}
//		return o;
//	}
//
//	public Object invoke(Object object, ExpNode node, boolean setter, String value)
//			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, H2JFilterException {
//		Method[] classMethods;
//		Method method;
//		boolean found;
//
//		if (node.getType() == Type.Property) {
//			// trasformo il nome in getter o setter
//			if (setter && (node.getProperty() == null)) {
//				ExpNode par;
//
//				par = new ExpNode(value, Type.Property);
//				node.getParameterList().add(par);
//				node.setObjectName(this.adjustName("set", node.getObjectName()));
//
//			} else {
//				node.setObjectName(this.adjustName("get", node.getObjectName()));
//			}
//		}
//
//		int numPar = node.getParameterList().size();
//		classMethods = object.getClass().getMethods();
//		found = false;
//		for (int i = 0; i < classMethods.length; ++i) {
//			method = classMethods[i];
//			if (method.getName().equals(node.getObjectName()) && (method.getParameterCount() == numPar)) {
//				Object[] paramsVal = null;
//				int n;
//				ExpNode parNode;
//				String parType;
//				String parVal;
//				Object parObject;
//				ObjectCastModel ocm;
//
//				n = method.getParameterCount();
//				paramsVal = new Object[n];
//				for (int k = 0; k < paramsVal.length; ++k) {
//					parType = method.getParameters()[k].getParameterizedType().getTypeName();
//					parNode = node.getParameterList().get(k);
//					parVal = parNode.getObjectName();
//					if (parNode.getProperty() == null) {
//						ocm = componentCast.get(parType);
//						if (ocm != null) {
//							parObject = ocm.cast(parVal);
//							paramsVal[k] = this.removeApice(parObject);
//						} else {
//							if (parVal.startsWith(ObjectWrapper.MARKER)) {
//								paramsVal[k] = ObjectWrapper.read(parVal);
//							} else {
//								paramsVal[k] = parVal;
//							}
//						}
//					} else {
//						paramsVal[k] = this.invoke(parNode, false, null);
//					}
//				}
//
//				object = method.invoke(object, paramsVal);
//				object = this.getArrayElement(node, object);
//				found = true;
//				break;
//			}
//		}
//		if (!found) {
//			throw new H2JFilterException("Missing " + node.getObjectName());
//		}
//
//		// Elaboro la sotto proprietà
//		if (node.getProperty() != null) {
//			assertNotNull(object, "object " + node.getObjectName() + " is null");
//			object = this.invoke(object, node.getProperty(), setter, value);
//		}
//
//		return object;
//	}
//

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
	 * @param fullName
	 * @return
	 * @throws H2JFilterException
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

//	private void evalForHTMLCall(ExpNode node, boolean onlyLocal) throws H2JFilterException {
//		if (node != null) {
//
//			if ((this.peek(node.getObjectName()) != null) && onlyLocal) {
//				ObjectCastModel ocm;
//				Object o;
//
//				o = this.invoke(node, false, null);
//				ocm = componentCast.get(o.getClass().getName());
//				if (ocm == null) {
//					throw new H2JFilterException("cannot resolve expression " + node);
//				}
//				node.setObjectName(o.toString());
//				if (o instanceof String) {
//					node.setObjectName("'" + node.getObjectName() + "'");
//				}
//				node.setProperty(null);
//			} else {
//				Object oVal;
//				for (ExpNode n : node.getParameterList()) {
//					if (null != getCDIObject(n.getObjectName())) {
//						oVal = this.invoke(n, false, null);
//						if (oVal instanceof String) {
//							n.setObjectName("'" + oVal + "'");
//						} else {
//							n.setObjectName(oVal != null ? oVal.toString() : null);
//						}
//						n.setProperty(null);
//						n.setType(Type.Property);
//					} else {
//						this.evalForHTMLCall(n, true);
//					}
//				}
//				this.evalForHTMLCall(node.getProperty(), false);
//			}
//		}
//	}

//	private Object removeApice(Object o) {
//		if (o instanceof String) {
//			String str = (String) o;
//			if ((str != null) && (str.startsWith("'")) && (str.endsWith("'"))) {
//				str = str.substring(1, str.length() - 1);
//			}
//			o = str;
//		}
//		return o;
//	}
//
//	private String adjustName(String prefix, String name) {
//		return prefix + name.substring(0, 1).toUpperCase() + name.substring(1);
//	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static void setServletContext(ServletContext servletContext) {
		Enviroments.servletContext = servletContext;
	}

	public static ComponentCast getComponentCast() {
		return componentCast;
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
