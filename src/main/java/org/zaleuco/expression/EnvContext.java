package org.zaleuco.expression;

public interface EnvContext {

	public Object get(String name) throws InvokerException;
}
