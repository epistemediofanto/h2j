package org.brioscia.javaz.expression;

public interface EnvContext {

	public Object get(String name) throws InvokerException;
}
