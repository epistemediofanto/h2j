package org.zaleuco.h2j.mw;

import java.util.HashMap;

import org.zaleuco.h2j.filter.cast.Converter;

public class HtmlBindName extends Assertion {

	private static final long serialVersionUID = 1L;
	private static long generator = (int) (100 * Math.random());

	protected static boolean developmentMode = false;

	private HashMap<String, StoreObject> envName = new HashMap<String, StoreObject>();

	public String htmlName(String name, Converter converter) {
		String newName = "hj2" + next();
		if (name == null) {
			name = newName;
		}
		if (developmentMode) {
			newName = name;
		}
		this.envName.put(newName, new StoreObject(name, converter));
		return newName;
	}

	public StoreObject originalName(String name) {
		StoreObject sName = this.envName.get(name);
		if (sName == null) {
			sName = new StoreObject(name, null);
		}
		return sName;
	}

	public void clearBindName() {
		this.envName.clear();
	}

	private static long next() {
		generator += (100 * Math.random());
		return generator;
	}

	public class StoreObject {
		public Converter converter;
		public String name;

		public StoreObject(String name, Converter converter) {
			this.name = name;
			this.converter = converter;
		}
	}
}
