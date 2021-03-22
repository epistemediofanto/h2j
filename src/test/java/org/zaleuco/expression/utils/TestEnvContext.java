package org.zaleuco.expression.utils;

import java.util.HashMap;

import org.brioscia.javaz.expression.EnvContext;

public class TestEnvContext implements EnvContext {

	private HashMap<String, Object> env = new HashMap<String, Object>();

	public Object get(String name) {
		return this.env.get(name);
	}

	public void put(String name, Object o) {
		this.env.put(name, o);
	}
}
