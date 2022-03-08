package org.brioscia.javaz.h2j.filter.cast;

public class ArrayStringConverter implements Converter {
	
	public String toString(Object avalue) {
		String ret = "";		
		if (avalue!=null) {
			Object[] values = (Object[]) avalue;
			for (int i=0; i<values.length; ++i) {
				ret += i>0 ? "," + values[i] : values[i];
			}
		}
		return  ret;
	}

	public String[] fromString(String value) {
		return value.split(",");
	}

	public String getName() {
		return "String[]";
	}

}
