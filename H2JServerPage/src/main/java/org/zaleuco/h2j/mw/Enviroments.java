package org.zaleuco.h2j.mw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletContext;

import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.ExpNode.Type;

public class Enviroments extends LocalEnviroment {

	private ServletContext servletContext;
	private Trasnslator trasnslator;
	private ComponentCast componentCast;
	private String contextRoot;

	public Enviroments(ServletContext context) throws H2JFilterException {
		this.servletContext = context;
		this.trasnslator = new Trasnslator(this);
		this.componentCast = new ComponentCast();
		this.contextRoot = this.servletContext.getContextPath();
	}

	public String getValue(String element) throws H2JFilterException {
		Object o;
		o = this.getObject(element);
		return o != null ? o.toString() : "";
	}

	public Object getObject(String fullName) throws H2JFilterException {
		try {
			return this.invoke(fullName, false, null);
		} catch (H2JFilterException e) {
			throw new H2JFilterException(fullName, e);
		}
	}

	public Object setBean(String fullName, String value) throws H2JFilterException {
		try {
			return this.invoke(fullName, true, value);
		} catch (H2JFilterException e) {
			throw new H2JFilterException(fullName, e);
		}
	}

	private Object invoke(String fullName, boolean setter, String value) throws H2JFilterException {
		Object object;
		ExpNode node;

		node = ParseFunctionCall.process(fullName);
		assertNotNull(node, "missing element");
		assertTrue(node.getType() == Type.Property, "invalid function " + node); // non può essere una funzione

		object = this.invoke(node, setter, value);
		return object;
	}

	private Object invoke(ExpNode node, boolean setter, String value) throws H2JFilterException {
		Object object;

		try {

			object = this.peek(node.getObjectName());
			if (object == null) {
				object = this.getCDIObject(node.getObjectName());
			}
			if (object != null) {

				if (node.getProperty() != null) {
					object = this.invoke(object, node.getProperty(), setter, value);
				} else {
					assertTrue(node.getParameterList().size() == 0, "internal error, invalid function call " + node); // condizione già verificata
				}
			} else {
				assertTrue(node.getParameterList().size() == 0, "invalid function " + node);
				object = node.getObjectName();
			}

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new H2JFilterException(e);
		}
		return object;
	}

	public Object invoke(Object object, ExpNode node, boolean setter, String value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, H2JFilterException {
		Method[] classMethods;
		Method method;
		boolean found;

		if (node.getType() == Type.Property) {
			// trasformo il nome in getter o setter
			if (setter && (node.getProperty() == null)) {
				ExpNode par;

				par = new ExpNode(value, Type.Property);
				node.getParameterList().add(par);
				node.setObjectName(this.adjustName("set", node.getObjectName()));

			} else {
				node.setObjectName(this.adjustName("get", node.getObjectName()));
			}
		}

		int numPar = node.getParameterList().size();
		classMethods = object.getClass().getMethods();
		found = false;
		for (int i = 0; i < classMethods.length; ++i) {
			method = classMethods[i];

			if (method.getName().equals(node.getObjectName()) && (method.getParameterCount() == numPar)) {
				Object[] paramsVal = null;
				int n;
				ExpNode parNode;
				String parType;
				String parVal;
				Object parObject;
				ObjectCastModel ocm;

				n = method.getParameterCount();
				paramsVal = new Object[n];
				for (int k = 0; k < paramsVal.length; ++k) {
					parType = method.getParameters()[k].getParameterizedType().getTypeName();
					parNode = node.getParameterList().get(k);
//					if (parNode.getType() == Type.ObjectRef) {
//						paramsVal[k] = node.getObject();
//					} else {
					parVal = parNode.getObjectName();
					if (parNode.getProperty() == null) {
						ocm = this.componentCast.get(parType);
						if (ocm != null) {
							parObject = ocm.cast(parVal);
							paramsVal[k] = this.removeApice(parObject);
						} else {
							throw new H2JFilterException("Illegal argument " + parNode);
						}
					} else {
						paramsVal[k] = this.invoke(parNode, false, null);
					}
				}

				object = method.invoke(object, paramsVal);
				found = true;
				break;
			}
		}
		if (!found)

		{
			throw new H2JFilterException("Missing " + node.getObjectName());
		}

		// Elaboro la sotto proprietà
		if (node.getProperty() != null) {
			object = this.invoke(object, node.getProperty(), setter, value);
		}

		return object;
	}

	private Object getCDIObject(String objectName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, H2JFilterException {
		Object object = null;

		BeanManager bm;
		CreationalContext<?> ctx;
		Bean<?> bean;
		Set<Bean<?>> set;
		Iterator<Bean<?>> iter;

		bm = CDI.current().getBeanManager();
		set = bm.getBeans(objectName);
		if (set == null) {
			throw new H2JFilterException("object " + objectName + " not found");
		}
		iter = set.iterator();
		if (iter.hasNext()) {

			bean = iter.next();
			ctx = bm.createCreationalContext(bean);

			object = bm.getReference(bean, bean.getBeanClass(), ctx);
		} else {
//			throw new H2JFilterException("object " + objectName + " not found");
		}
		return object;
	}

	/**
	 * 
	 * prepara l'espressione da chiamara alla submit da html valuta solo gli
	 * elementi dell'ambiente locale
	 * 
	 * @param fullName
	 * @return
	 * @throws H2JFilterException
	 */
	public String evalForHTMLCall(String fullName) throws H2JFilterException {
		int posStart;
		int posEnd;
		ExpNode node;

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
					node = ParseFunctionCall.process(exp);
					this.evalForHTMLCall(node, false);
					value = node.toString();

					fullName = fullName.replace(fullExp, value);
					continue;
				}
			}
			break;
		}

//		Node node;
//
//		if (fullName != null) {
//			node = ParseFunctionCall.process(fullName);
//			this.evalForHTMLCall(node, false);
//			fullName = node.toString();
//		}

		return fullName;
	}

	private void evalForHTMLCall(ExpNode node, boolean onlyParameters) throws H2JFilterException {
		if (node != null) {

			if ((this.peek(node.getObjectName()) != null) && !onlyParameters) {
				ObjectCastModel ocm;
				Object o;

				o = this.invoke(node, false, null);
				ocm = this.componentCast.get(o.getClass().getName());
				if (ocm == null) {
					throw new H2JFilterException("cannot resolve expression " + node);
				}
				node.setObjectName(o.toString());
				if (o instanceof String) {
					node.setObjectName("'" + node.getObjectName() + "'");
				}
				node.setParameterList(new ArrayList<ExpNode>());
				node.setProperty(null);
			} else {
				for (ExpNode n : node.getParameterList()) {
					this.evalForHTMLCall(n, false);
				}
				this.evalForHTMLCall(node.getProperty(), true);
			}
		}
	}

	private Object removeApice(Object o) {
		if (o instanceof String) {
			String str = (String) o;
			if ((str != null) && (str.startsWith("'")) && (str.endsWith("'"))) {
				str = str.substring(1, str.length() - 1);
			}
			o = str;
		}
		return o;
	}

	private String adjustName(String prefix, String name) {
		return prefix + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ComponentCast getComponentCast() {
		return componentCast;
	}

	public String getContextRoot() {
		return contextRoot;
	}

	public Trasnslator getTrasnslator() {
		return trasnslator;
	}

}
