package org.zaleuco.h2j.filter.cast;

import java.util.Date;
import java.util.HashMap;

public class Shape {

	public static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static HashMap<String, Converter> adapters = new HashMap<String, Converter>();

	public static void initCommonConverter() {

		add(int.class.getCanonicalName(), new IntegerConverter());
		add(Integer.class.getCanonicalName(), new IntegerConverter());
		add(Date.class.getCanonicalName(), new DateConverter(ISO_8601));
		add("dmyDate", new DateConverter("dd/MM/yyyy"));
		add("ymdDate", new DateConverter("yyyy/MM/dd"));
		add("mdyDate", new DateConverter("MM/dd/yyyy"));
		add(boolean.class.getCanonicalName(), new BooleanConverter());
		add(Boolean.class.getCanonicalName(), new BooleanConverter());

	}

	public static void add(String className, Converter converter) {
		adapters.put(className, converter);
	}

	public static void remove(String className) {
		adapters.remove(className);
	}

	public static Converter get(String className) {
		return adapters.get(className);
	}

	public static Converter getConverter(Object o) {
		return adapters.get(o.getClass().getCanonicalName());
	}

	public static String toHTML(Object value) {
		String out = null;
		if (value != null) {
			Converter converter = adapters.get(value.getClass().getCanonicalName());
			if (converter != null) {
				out = converter.toString(value);
			} else {
				out = value.toString();
			}
		}
		return out;
	}

	public static Object fromHTML(String value) {
		Object object = null;
		if ((value != null) && (value.length() > 0)) {
			Converter converter = adapters.get(value.getClass().getCanonicalName());
			System.out.println(value);
			object = converter.fromString(value);
		}
		return object;
	}
}
