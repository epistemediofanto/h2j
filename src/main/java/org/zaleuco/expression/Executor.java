package org.zaleuco.expression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import org.zaleuco.expression.NodeToken.Type;

public class Executor {

	private EnvContext context;
	private Object value;
	private boolean setter = false;
	private ComponentCast cast = new ComponentCast();

	public static Object get(NodeToken node, EnvContext context) throws SyntaxError, InvokerException {
		Executor e;
		e = new Executor();
		e.context = context;
		return e.eval(node);
	}

	public static Object set(NodeToken node, EnvContext context, Object value) throws SyntaxError, InvokerException {
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
			throw new SyntaxError(node, "unsupported operation");

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
			o = this.convert(node);
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
		if (a instanceof String) {
			if (b instanceof String) {
				r = (String) a + (String) b;
			} else {
				r = (String) a + (b != null ? b.toString() : "null");
			}
		} else if ((a instanceof Integer) && (b instanceof Integer)) {
			r = (int) a + (int) b;
		} else if ((a instanceof Integer) && (b instanceof Double)) {
			r = (int) a + (double) b;
		} else if ((a instanceof Double) && (b instanceof Integer)) {
			r = (double) a + (int) b;
		} else if ((a instanceof Long) && (b instanceof Long)) {
			r = (Long) a + (Long) b;
		} else {
			throw new SyntaxError(node, "unsupported operation");
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
			throw new SyntaxError(node, "unsupported operation");
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
			throw new SyntaxError(node, "unsupported operation");
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
			throw new SyntaxError(node, "unsupported operation");
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
			throw new SyntaxError(node, "unsupported operation");
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
			throw new SyntaxError(node, "unsupported operation");
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
			throw new SyntaxError(node, "unsupported operation");
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
			throw new SyntaxError(node, "unsupported operation");
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
			throw new SyntaxError(node, "unsupported operation");
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
			throw new SyntaxError(node, "unsupported operation");
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
			throw new SyntaxError(node, "unsupported operation");
		}
		return r;
	}

	private Object not(NodeToken node) throws SyntaxError, InvokerException {
		Object a, r;
		a = this.eval(node.getChilds().get(0));

		if (a instanceof Boolean) {
			r = !(Boolean) a;
		} else {
			throw new SyntaxError(node, "unsupported operation");
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
			throw new SyntaxError(node, "unsupported operation");
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
				} else // if ((v.length() > 2) && (v.startsWith("'")) && (v.endsWith("'"))) {
//					return new String(v.substring(1, v.length() - 1));
					return v;
//				}
				// throw new SyntaxError(node, "unsupported operation");
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
			object = this.invoke(object, node);
		} else if (this.value != null) {
			throw new SyntaxError(node, "unsupported operation (set value) ");
		}

		return object;
	}

	private Object invoke(Object object, NodeToken node) throws SyntaxError, InvokerException {
		Method[] classMethods;
		Method method;
		boolean found;
		String propertyName;
		int childPos = 0;
		List<NodeToken> childs;
		NodeToken nodeParams = null;
		int numParams = 0;
		Object old;
		boolean setter;

		assertTrue(object != null, node, "can't set/get property, object is null");

		childs = node.getChilds();
		classMethods = object.getClass().getMethods();
		found = false;
		setter = this.setter && (node.getChilds().size() == 0);

		if ((childs.size() > childPos) && (childs.get(childPos).getType() == Type.function)) {
			nodeParams = childs.get(childPos);
			numParams = nodeParams.getChilds().size();
			propertyName = node.getValue();
			++childPos;
		} else if (setter) {
			propertyName = adjustName("set", node.getValue());
			numParams = 1;
		} else {
			propertyName = adjustName("get", node.getValue());
		}

		for (int i = 0; i < classMethods.length; ++i) {
			method = classMethods[i];
			if (method.getName().equals(propertyName) && (method.getParameterCount() == numParams)) {
				Object[] paramValue;
				Parameter[] parameters;

				parameters = method.getParameters();
				paramValue = new Object[numParams];
				if (setter) {
					paramValue[0] = this.cast(parameters[0], this.value);
				} else {
					for (int j = 0; j < numParams; ++j) {
						this.value = this.eval(nodeParams.getChilds().get(j));
						paramValue[j] = this.value != null ? this.cast(parameters[j], this.value) : null;
					}
				}

				try {
					old = object;
					object = method.invoke(old, paramValue);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new InvokerException(node, e);
				}
				found = true;
				break;
			}
		}

		if (!found) {
			throw new InvokerException(node, "Missing property or function: " + node.getValue());
		}

		while ((childs.size() > childPos) && (childs.get(childPos).getType() == Type.arrayIndex)) {
			nodeParams = childs.get(childPos);
			numParams = nodeParams.getChilds().size();
			for (int i = 0; i < numParams; ++i) {
				Integer ix = (Integer) this.eval(nodeParams.getChilds().get(i));
				object = this.getIndex(node, object, ix);
			}
			++childPos;
		}

		if ((childs.size() > childPos) && (childs.get(childPos).getType() == Type.dot)) {
			nodeParams = childs.get(childPos);
			assertTrue(nodeParams.getChilds().size() == 1, nodeParams, "unsupported operation");
			object = this.invoke(object, nodeParams.getChilds().get(0));
			++childPos;
		}

		assertTrue(childs.size() == childPos, node, "invalid operation");

		return object;
	}

	private Object cast(Parameter parameter, Object object) throws SyntaxError {
		ObjectCastModel ocm;
		String parType;

		if (object != null) {
			parType = parameter.getParameterizedType().getTypeName();
			if (!parType.equals(object.getClass().getCanonicalName())) {
				ocm = this.cast.get(parType);
				if (ocm != null) {
					object = ocm.cast(object.toString());
				}
			}
		}

		return object;
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

	private void assertTrue(boolean b, NodeToken token, String message) throws SyntaxError {
		if (!b) {
			throw new SyntaxError(token, message);
		}
	}

	private String adjustName(String prefix, String name) {
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
