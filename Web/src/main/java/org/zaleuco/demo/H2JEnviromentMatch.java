package org.zaleuco.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletContext;

import org.zaleuco.expression.ObjectCastModel;
import org.zaleuco.h2j.filter.H2JFilterException;

public class H2JEnviromentMatch {

	private HashMap<String, ObjectCastModel> castComponents;
	private ServletContext context;

	public H2JEnviromentMatch(ServletContext servletContext) throws H2JFilterException {

		this.context = servletContext;
		this.castComponents = new HashMap<String, ObjectCastModel>();

		this.castComponents.put("int", (String value) -> {
			return Integer.parseInt(value);
		});

		this.castComponents.put("java.lang.String", (String value) -> {
			return value;
		});

	}

	public void registerComponentCast(String typeName, ObjectCastModel cast) {
		this.castComponents.put(typeName, cast);
	}

	public void unregisterComponentCast(String typeName) {
		this.castComponents.remove(typeName);
	}

	public String getValue(String element) {
		Object object = this.getObjectValue(element);
		return object != null ? object.toString() : null;
	}

	public Object getObjectValue(String element) {
		Object object;
		String fieldName;
		Method method;
		String[] elements;

		try {
			elements = this.getNames(element);
			object = this.getObject(elements);
			if (elements.length > 0) {
				fieldName = elements[elements.length - 1];
				method = this.getMethod(object, "get", fieldName);
				object = method.invoke(object);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
		return object;
	}

	public void setBean(String element, String fieldStringValue) throws H2JFilterException {
		Object object;
		ObjectCastModel cast;
		Method method;
		Parameter parameter;
		String fieldName;
		String parClassName;
		Object value;
		String[] elements;

		try {
			elements = this.getNames(element);
			object = this.getObject(elements);

			fieldName = elements[elements.length - 1];
			method = this.getMethod(object, "set", fieldName);

			parameter = method.getParameters()[0];
			parClassName = parameter.getParameterizedType().getTypeName();
			cast = this.castComponents.get(parClassName);
//			value = cast.cast(fieldStringValue);
//			method.invoke(object, value);

		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private Method getMethod(Object object, String prefix, String fieldName) {
		String methodName;
		Method[] methods;
		Method method;
		Method ret = null;

		methodName = this.getMethod(prefix, fieldName);
		methods = object.getClass().getMethods();
		for (int i = 0; i < methods.length; ++i) {
			method = methods[i];
			if (methodName.equals(method.getName())) {
				ret = method;
				break;
			}
		}
		if (ret == null) {
			throw new IllegalArgumentException("field not found: " + fieldName);
		}

		return ret;
	}

	private Object getObject(String[] elements)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String beanObject;
		Object object = null;
		String element;

		element = elements[0];
		beanObject = this.getName(element);
		if (beanObject != null) {
			BeanManager bm;
			CreationalContext<?> ctx;
			Bean<?> bean;
			Method method;

			bm = CDI.current().getBeanManager();
			bean = bm.getBeans(beanObject).iterator().next();
			ctx = bm.createCreationalContext(bean);

			object = bm.getReference(bean, bean.getBeanClass(), ctx);

			for (int ix = 1; ix < elements.length - 1; ++ix) {
				method = this.getMethod(object, "get", elements[ix]);
				object = method.invoke(object);
			}

		} else {
			throw new IllegalArgumentException("field not found: " + element);
		}
		return object;
	}

	private String[] getNames(String element) {
		return element.split("\\.");
	}

	private String getMethod(String prefix, String fieldName) {
		return prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	private String getName(String beanName) {
		String name = null;
		if (beanName != null) {
			int pos = beanName.indexOf('.');
			if (pos != -1) {
				name = beanName.substring(0, pos);
			} else {
				name = beanName;
			}
		}
		return name;
	}

	public ServletContext getContext() {
		return context;
	}

	public void setContext(ServletContext context) {
		this.context = context;
	}

}
