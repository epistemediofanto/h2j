package org.zaleuco.h2j.mw;

import java.io.Serializable;
import java.util.HashMap;

import org.zaleuco.h2j.filter.cast.Converter;

public class HtmlBindName extends Assertion {

	private static final long serialVersionUID = 1L;
	private static long generator = (int) (100 * Math.random());

	protected static boolean developmentMode = false;

	private HashMap<String, StoreObject> envName = new HashMap<String, StoreObject>();

	public String htmlName(String name, Converter converter, int type) {
		String newName = "hj2" + next();
		if (name == null) {
			name = newName;
		}
		if (developmentMode) {
			newName = name;
		}
		this.envName.put(newName, new StoreObject(name, converter, type));
		return newName;
	}

	public StoreObject getObjectFromNameStore(String name) {
		StoreObject sName;
//		int n;
//		char c;
//		int lastPos = -1;
//		String path;
//		String name;
//
//		n = page.length();
//		for (int i = 0; i < n; ++i) {
//			c = page.charAt(i);
//			if (c == '/') {
//				lastPos = i;
//			} else if (c == '(') {
//				break;
//			}
//		}
//
//		path = page.substring(0, lastPos + 1);
//		name = page.substring(lastPos + 1, n - H2JProcessorFilter.EXT.length());

		sName = this.envName.get(name);
		if (sName == null) {
			sName = new StoreObject(name, null, HtmlBindName.OBJECT);
		}
		return sName;
	}

	@Deprecated
	public StoreObject originalName(String name) {
		StoreObject sName = this.envName.get(name);
		if (sName == null) {
			sName = new StoreObject(name, null, HtmlBindName.OBJECT);
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

	public static final int DYNAMIC_CALL = 1;
	public static final int OBJECT = 2;

	public class StoreObject implements Serializable {
		private static final long serialVersionUID = 1L;
		public Converter converter;
		public String name;
		public int type;

		public StoreObject(String name, Converter converter, int type) {
			this.name = name;
			this.converter = converter;
			this.type = type;
		}
	}
}
