package org.brioscia.javaz.h2j.filter.cast;

public class ArrayIntegerConverter implements Converter {

	public String toString(Object avalue) {
		String ret = "";		
		if (avalue != null) {
			Object[] values = (Object[]) avalue;
			for (int i = 0; i < values.length; ++i) {
				ret += i > 0 ? "," + values[i] : values[i];
			}
		}
		return ret;
	}

	public Integer[] fromString(String value) {
		Integer[] ints = null;
		if (value != null) {
			String[] varstring = value.split(",");
			ints = new Integer[varstring.length];
			for (int i = 0; i < varstring.length; ++i) {
				ints[i] = Integer.parseInt(varstring[i]);
			}
		}
		return ints;
	}

	public String getName() {
		return "String[]";
	}

}
