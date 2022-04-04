package org.brioscia.javaz.expression;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import org.brioscia.javaz.expression.NodeToken.Type;
import org.brioscia.javaz.h2j.mw.Enviroments;
import org.brioscia.javaz.h2j.tag.ListType;

public class Executor {

	private EnvContext context;
	private Object[] value;
	private boolean setter = false;
	private ComponentCast cast = new ComponentCast();

	public static Object get(NodeToken node, EnvContext context) throws SyntaxError, InvokerException {
		Executor e;
		e = new Executor();
		e.context = context;
		return e.eval(node);
	}

	public static Object set(NodeToken node, EnvContext context, Object[] value) throws SyntaxError, InvokerException {
		Executor e;
		e = new Executor();
		e.context = context;
		e.value = value;
		e.setter = true;
		return e.eval(node);
	}

	private Object eval(NodeToken node) throws SyntaxError, InvokerException {
		Object o = null;

		switch (node.getType()) {
		case add:
			o = this.add(node);
			break;

		case and:
			o = this.and(node);
			break;

		case arrayIndex:
			throw new SyntaxError(node, "invalid operation");

		case div:
			o = this.div(node);
			break;

		case dot:
			throw new SyntaxError(node, "invalid operation");

		case eq:
			o = this.eq(node);
			break;

		case function:
			throw new SyntaxError(node, "invalid operation");

		case get:
			o = this.get(node);
			break;

		case gt:
			o = this.gt(node);
			break;

		case let:
			o = this.let(node);
			break;

		case lt:
			o = this.lt(node);
			break;

		case mul:
			o = this.mul(node);
			break;

		case name:
			o = this.name(node);
			break;
		case neg:
			o = this.neg(node);
			break;

		case neq:
			o = this.neq(node);
			break;

		case nil:
			o = null;
			break;

		case not:
			o = this.not(node);
			break;

		case number:
			o = this.convert(node);
			break;

		case or:
			o = this.or(node);
			break;

		case pow:
			o = this.pow(node);
			break;

		case string:
			// o = this.convert(node); // path del 15/01/2022 errore in ajax converte in int
			// le stringhe
			o = node.getValue();
			break;

		case sub:
			o = this.sub(node);
			break;

		case undef:
			throw new SyntaxError(node, "undefined type ");
		}

		return o;
	}

	private Object add(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a == null) || (a instanceof String)) {
			String sa = (a == null) ? "" : (String) a;
			String sb = (b == null) ? "" : b.toString();
			r = sa + sb;
		} else if ((a instanceof Integer) && (b instanceof Integer)) {
			r = (int) a + (int) b;
		} else if ((a instanceof Integer) && (b instanceof Double)) {
			r = (int) a + (double) b;
		} else if ((a instanceof Double) && (b instanceof Integer)) {
			r = (double) a + (int) b;
		} else if ((a instanceof Long) && (b instanceof Long)) {
			r = (Long) a + (Long) b;
		} else {
			throw new SyntaxError(node, "unsupported operation: (" + a + ") - (" + b + ")");
		}
		return r;
	}

	private Object sub(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a instanceof Integer) && (b instanceof Integer)) {
			r = (int) a - (int) b;
		} else if ((a instanceof Integer) && (b instanceof Double)) {
			r = (int) a - (double) b;
		} else if ((a instanceof Double) && (b instanceof Integer)) {
			r = (double) a - (int) b;
		} else if ((a instanceof Long) && (b instanceof Long)) {
			r = (Long) a - (Long) b;
		} else {
			throw new SyntaxError(node, "unsupported operation: (" + a + ") - (" + b + ")");
		}
		return r;
	}

	private Object mul(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a instanceof Integer) && (b instanceof Integer)) {
			r = (int) a * (int) b;
		} else if ((a instanceof Integer) && (b instanceof Double)) {
			r = (int) a * (double) b;
		} else if ((a instanceof Double) && (b instanceof Integer)) {
			r = (double) a * (int) b;
		} else if ((a instanceof Long) && (b instanceof Long)) {
			r = (Long) a * (Long) b;
		} else {
			throw new SyntaxError(node, "unsupported operation: (" + a + ") * (" + b + ")");
		}
		return r;
	}

	private Object div(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a instanceof Integer) && (b instanceof Integer)) {
			r = (int) a / (int) b;
		} else if ((a instanceof Integer) && (b instanceof Double)) {
			r = (int) a / (double) b;
		} else if ((a instanceof Double) && (b instanceof Integer)) {
			r = (double) a / (int) b;
		} else if ((a instanceof Long) && (b instanceof Long)) {
			r = (Long) a / (Long) b;
		} else {
			throw new SyntaxError(node, "unsupported operation: (" + a + ") / (" + b + ")");
		}
		return r;
	}

	private Object pow(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a instanceof Integer) && (b instanceof Integer)) {
			r = Math.pow((int) a, (int) b);
		} else if ((a instanceof Integer) && (b instanceof Double)) {
			r = Math.pow((int) a, (double) b);
		} else if ((a instanceof Double) && (b instanceof Integer)) {
			r = Math.pow((double) a, (int) b);
		} else if ((a instanceof Long) && (b instanceof Long)) {
			r = Math.pow((Long) a, (Long) b);
		} else {
			throw new SyntaxError(node, "unsupported operation: (" + a + ") ^ (" + b + ")");
		}
		return r;
	}

	private Object lt(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a instanceof Integer) && (b instanceof Integer)) {
			r = (int) a < (int) b;
		} else if ((a instanceof Integer) && (b instanceof Double)) {
			r = (int) a < (double) b;
		} else if ((a instanceof Double) && (b instanceof Integer)) {
			r = (double) a < (int) b;
		} else if ((a instanceof Long) && (b instanceof Long)) {
			r = (Long) a < (Long) b;
		} else {
			throw new SyntaxError(node, "unsupported operation: (" + a + ") < (" + b + ")");
		}
		return r;
	}

	private Object let(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a instanceof Integer) && (b instanceof Integer)) {
			r = (int) a <= (int) b;
		} else if ((a instanceof Integer) && (b instanceof Double)) {
			r = (int) a <= (double) b;
		} else if ((a instanceof Double) && (b instanceof Integer)) {
			r = (double) a <= (int) b;
		} else if ((a instanceof Long) && (b instanceof Long)) {
			r = (Long) a <= (Long) b;
		} else {
			throw new SyntaxError(node, "unsupported operation: (" + a + ") <= (" + b + ")");
		}
		return r;
	}

	private Object get(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a instanceof Integer) && (b instanceof Integer)) {
			r = (int) a >= (int) b;
		} else if ((a instanceof Integer) && (b instanceof Double)) {
			r = (int) a >= (double) b;
		} else if ((a instanceof Double) && (b instanceof Integer)) {
			r = (double) a >= (int) b;
		} else if ((a instanceof Long) && (b instanceof Long)) {
			r = (Long) a >= (Long) b;
		} else {
			throw new SyntaxError(node, "unsupported operation: (" + a + ") >= (" + b + ")");
		}
		return r;
	}

	private Object gt(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a instanceof Integer) && (b instanceof Integer)) {
			r = (int) a > (int) b;
		} else if ((a instanceof Integer) && (b instanceof Double)) {
			r = (int) a > (double) b;
		} else if ((a instanceof Double) && (b instanceof Integer)) {
			r = (double) a > (int) b;
		} else if ((a instanceof Long) && (b instanceof Long)) {
			r = (Long) a > (Long) b;
		} else {
			throw new SyntaxError(node, "unsupported operation: (" + a + ") > (" + b + ")");
		}
		return r;
	}

	private Object eq(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a instanceof Integer) && (b instanceof Integer)) {
			r = (int) a == (int) b;
		} else if ((a instanceof Integer) && (b instanceof Double)) {
			r = (int) a == (double) b;
		} else if ((a instanceof Double) && (b instanceof Integer)) {
			r = (double) a == (int) b;
		} else if ((a instanceof Long) && (b instanceof Long)) {
			r = (long) a == (long) b;
		} else if ((a instanceof Boolean) && (b instanceof Boolean)) {
			r = (boolean) a == (boolean) b;
		} else {
			r = (a != null) ? a.equals(b) : b == null;
		}
		return r;
	}

	private Object neq(NodeToken node) throws SyntaxError, InvokerException {
		Boolean b = (Boolean) this.eq(node);
		return b ? Boolean.FALSE : Boolean.TRUE;
	}

	private Object and(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a instanceof Boolean) && (b instanceof Boolean)) {
			r = (Boolean) a && (Boolean) b;
		} else {
			throw new SyntaxError(node, "unsupported operation: (" + a + ") && (" + b + ")");
		}
		return r;
	}

	private Object or(NodeToken node) throws SyntaxError, InvokerException {
		Object a, b, r;
		a = this.eval(node.getChilds().get(0));
		b = this.eval(node.getChilds().get(1));
		if ((a instanceof Boolean) && (b instanceof Boolean)) {
			r = (Boolean) a && (Boolean) b;
		} else {
			throw new SyntaxError(node, "unsupported operation: (" + a + ") || (" + b + ")");
		}
		return r;
	}

	private Object not(NodeToken node) throws SyntaxError, InvokerException {
		Object a, r;
		a = this.eval(node.getChilds().get(0));

		if (a instanceof Boolean) {
			r = !(Boolean) a;
		} else {
			throw new SyntaxError(node, "unsupported operation: !(" + a + ")");
		}
		return r;
	}

	private Object neg(NodeToken node) throws SyntaxError, InvokerException {
		Object a, r;
		a = this.eval(node.getChilds().get(0));

		if (a instanceof Integer) {
			r = -(Integer) a;
		} else if (a instanceof Double) {
			r = -(Double) a;
		} else if (a instanceof Long) {
			r = -(Long) a;
		} else {
			throw new SyntaxError(node, "unsupported operation: - (" + a + ")");
		}
		return r;
	}

	private Object convert(NodeToken node) throws SyntaxError {
		try {
			return Integer.parseInt(node.getValue());
		} catch (NumberFormatException e) {
			try {
				return Double.parseDouble(node.getValue());
			} catch (NumberFormatException ex) {
				String v = node.getValue();
				if ("true".equals(v)) {
					return true;
				} else if ("false".equals(v)) {
					return false;
				} else {
					return v;
				}
			}
		}
	}

	private Object name(NodeToken node) throws SyntaxError, InvokerException {
		Object object;

		assertTrue(node.getChilds().size() <= 1, node, "invalid operation");

		object = this.context.get(node.getValue());
		if (object == null) {
			object = this.evalParameters(node);
		} else if (node.getChilds().size() == 1) {
			node = node.getChilds().get(0);
			assertTrue(node.getType() == Type.dot, node, "unsupported operation");
			assertTrue(node.getChilds().size() == 1, node, "unsupported operation");
			node = node.getChilds().get(0);
			if ((object instanceof String) && (Enviroments.isELName((String) object))) {
				String str = (String) object;
				str = str.substring(2);
				str = str.substring(0, str.length() - 1);
				object = this.context.get(str);
			}
			object = this.invoke(object, node);
		} else if (this.value != null) {
			throw new SyntaxError(node, "the operation cannot be performed, invalid setter call");
		}

		return object;
	}

	private Object invoke(Object object, NodeToken node) throws SyntaxError, InvokerException {
		int childPos = 0;
		int numParams = 0;
		boolean isSetter;
		String nodeValue;
		String propertyName;
		List<NodeToken> childs;
		NodeToken nodeParams = null;
		Object newObject = object;

		assertTrue(object != null, node, "class method not found or object is null");

		nodeValue = node.getValue();

		childs = node.getChilds();
		// sono in modalità scrittura e l'oggetto non ha più figli
		isSetter = this.setter && (node.getChilds().size() == 0);

		if ((childs.size() > childPos) && (childs.get(childPos).getType() == Type.function)) {
			// chiamata a funzione es: fun(i, j)
			++childPos;
			newObject = this.invokeFunction(object, node, nodeValue);
		} else if (isSetter) {
			propertyName = adjustName("set", nodeValue);
			numParams = 1;
			newObject = this.invokeSetter(object, node, propertyName);
		} else {
			propertyName = adjustName("get", nodeValue);
			newObject = this.invokeGetter(object, node, propertyName);
		}

		while ((childs.size() > childPos) && (childs.get(childPos).getType() == Type.arrayIndex)) {
			// siamo in un array, identifichiamo l'indice
			nodeParams = childs.get(childPos);
			numParams = nodeParams.getChilds().size();
			for (int i = 0; i < numParams; ++i) {
				Integer ix = (Integer) this.eval(nodeParams.getChilds().get(i));
				newObject = this.getIndex(node, newObject, ix);
			}
			++childPos;
		}

		if ((childs.size() > childPos) && (childs.get(childPos).getType() == Type.dot)) {
			// xxx.yyy vado in ricorsione su yyy
			nodeParams = childs.get(childPos);
			assertTrue(nodeParams.getChilds().size() == 1, nodeParams, "unsupported operation");
			newObject = this.invoke(newObject, nodeParams.getChilds().get(0));
			++childPos;
		}

		assertTrue(childs.size() == childPos, node, "invalid operation");

		return newObject;
	}

	private Object invokeFunction(Object object, NodeToken node, String methodName)
			throws SyntaxError, InvokerException {
		Method method;
		Method[] classMethods;
		int numParams;
		List<NodeToken> childs;
		NodeToken nodeParams = null;

		String propertyName = methodName;

		childs = node.getChilds();
		nodeParams = childs.get(0);
		numParams = nodeParams.getChilds().size();

		classMethods = object.getClass().getMethods();
		for (int i = 0; i < classMethods.length; ++i) {
			method = classMethods[i];
			if ((method.getName().equals(propertyName) && (method.getParameterCount() == numParams))) {
				Object[] paramValue;
				Parameter[] parameters;
				Object value;

				parameters = method.getParameters();
				paramValue = new Object[numParams];
				try {
					for (int j = 0; j < numParams; ++j) {
						value = this.eval(nodeParams.getChilds().get(j));
						paramValue[j] = value != null ? this.cast(parameters[j], new Object[] { value }) : null;
					}
					object = method.invoke(object, paramValue);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new InvokerException(node, e);
				}
				return object;
			}
		}

		throw new InvokerException(node, "Missing method: " + methodName);
	}

	private Object invokeGetter(Object object, NodeToken node, String methodName) throws InvokerException {
		Method[] classMethods;
		Method method;

		classMethods = object.getClass().getMethods();
		for (int i = 0; i < classMethods.length; ++i) {
			method = classMethods[i];
			if ((method.getParameterCount() == 0) && this.isGetter(method, methodName)) {
				Object[] paramValue;

				paramValue = new Object[0];
				try {
					object = method.invoke(object, paramValue);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new InvokerException(node, e);
				}
				return object;
			}
		}

		throw new InvokerException(node, "Missing getter: " + methodName);
	}

	private Object invokeSetter(Object object, NodeToken node, String methodName) throws InvokerException, SyntaxError {
		if (object instanceof List) {
			try {
				Object item;
				List<?> objectList = (List<?>) object;
				if (this.value instanceof Object[]) {
					Object[] arrValue = (Object[]) this.value;
					Object[] value;
					String type = null;
					value = new Object[1];
					for (int i = 0; i < arrValue.length; ++i) {
						value[0] = arrValue[i];
						if (i == objectList.size()) {
							if (type == null) {
								type = this.getListType(objectList, methodName);
							}
							item = this.newInstance(node, type, objectList);
							Method add = List.class.getDeclaredMethod("add", Object.class);
							add.invoke(objectList, item);
						} else {
							item = objectList.get(i);
						}
						this.set(node, item, value, methodName);
					}
					return object;
				} else {
					if (1 < objectList.size()) {
						item = this.newInstance(node, this.getListType(objectList, methodName), objectList);
						Method add = List.class.getDeclaredMethod("add", Object.class);
						add.invoke(objectList, item);
					} else {
						item = objectList.get(0);
					}
					this.set(node, item, this.value, methodName);
					return object;
				}
			} catch (Exception e) {
				throw new InvokerException(node, e);
			}
		} else {
			return this.set(node, object, this.value, methodName);
		}
	}

	private String getListType(Object parent, String fieldName) throws NoSuchFieldException, SecurityException {
		Class<?> clazz = parent.getClass();
		ListType listType = clazz.getAnnotation(ListType.class);
		String type = listType.type();
		return type;
	}

	private Object newInstance(NodeToken nodeToken, String className, Object object)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		return Class.forName(className).getDeclaredConstructor().newInstance();
	}

	private Object set(NodeToken node, Object object, Object[] value, String methodName)
			throws InvokerException, SyntaxError {
		Method[] classMethods;
		Method method;

		classMethods = object.getClass().getMethods();
		for (int i = 0; i < classMethods.length; ++i) {
			method = classMethods[i];
			if ((method.getName().equals(methodName) && (method.getParameterCount() == 1))) {
				Object[] paramValue;
				Parameter[] parameters;

				parameters = method.getParameters();
				paramValue = new Object[1];
				try {
					paramValue[0] = this.cast(parameters[0], value);
					object = method.invoke(object, paramValue);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new InvokerException(node, e);
				}
				return object;
			}
		}
		throw new InvokerException(node, "Missing setter " + methodName);
	}

	private boolean isGetter(Method method, String propertyName) {
		boolean is = false;
		is = method.getName().equals(propertyName);
		if (!is && (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class))) {
			String name;
			name = method.getName();
			is = name.startsWith("is") && (propertyName.startsWith("get"))
					&& name.substring(2).equals(propertyName.substring(3));
		}
		return is;
	}

	private Object cast(Parameter parameter, Object[] object) throws SyntaxError {
		ObjectCastModel ocm;
		String parType;
		Object o = object;
		if (object != null) {
			parType = parameter.getParameterizedType().getTypeName();
			if (!parType.equals(object.getClass().getCanonicalName())) {
				ocm = this.cast.get(parType);
				if (ocm != null) {
					o = ocm.cast(object);
				}
			}
		}

		return o;
	}

	private Object getIndex(NodeToken nodeToken, Object object, int ix) throws InvokerException {
		Class<?> cls = object.getClass();
		if (!cls.isArray()) {
			throw new InvokerException(nodeToken, "invalid array");
		}
		switch (cls.getName()) {
		case "[B":
			object = ((byte[]) object)[ix];
			break;
		case "[C":
			object = ((char[]) object)[ix];
			break;
		case "[I":
			object = ((int[]) object)[ix];
			break;
		case "[F":
			object = ((float[]) object)[ix];
			break;
		case "[D":
			object = ((double[]) object)[ix];
			break;
		case "[Z":
			object = ((boolean[]) object)[ix];
			break;
		default:
			object = ((Object[]) object)[ix];
		}
		return object;
	}

	private static final void assertTrue(boolean b, NodeToken token, String message) throws SyntaxError {
		if (!b) {
			throw new SyntaxError(token, message);
		}
	}

	private static String adjustName(String prefix, String name) {
		return prefix + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private Object evalParameters(NodeToken node) throws SyntaxError, InvokerException {
		int childPos = 0;
		List<NodeToken> childs;
		Object object;

		childs = node.getChilds();

		if ((childs.size() > childPos) && (childs.get(childPos).getType() == Type.function)) {
			for (NodeToken n : childs.get(0).getChilds()) {
				object = this.eval(n);
				if ((object instanceof String) || (object instanceof Character)) {
					n.setType(Type.string);
				} else {
					n.setType(Type.name);
				}
				n.setValue(object != null ? object.toString() : null);
				n.resetChild();
				++childPos;
			}
		}
		if ((childs.size() > childPos) && (childs.get(childPos).getType() == Type.arrayIndex)) {
			for (NodeToken n : childs.get(0).getChilds()) {
				object = this.eval(n);
				if ((object instanceof String) || (object instanceof Character)) {
					n.setType(Type.string);
				} else {
					n.setType(Type.name);
				}
				n.setValue(object != null ? object.toString() : null);
				n.resetChild();
				++childPos;
			}
		}
		if ((childs.size() > childPos) && (childs.get(childPos).getType() == Type.dot)) {
			this.evalParameters(childs.get(childPos).getChilds().get(0));
		}

		return node;
	}
}
