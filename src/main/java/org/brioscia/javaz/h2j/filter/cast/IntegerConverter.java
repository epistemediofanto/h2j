package org.brioscia.javaz.h2j.filter.cast;

public class IntegerConverter implements Converter {

	public String toString(Object value) {
		return String.valueOf(value);
	}

	public Integer fromString(String value) {
		return value != null ? Integer.parseInt(value) : null;
	}
	
	public String getName() {
		return "Integer";
	}


}
