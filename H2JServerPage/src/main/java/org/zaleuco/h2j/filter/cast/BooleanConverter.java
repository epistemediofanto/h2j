package org.zaleuco.h2j.filter.cast;

public class BooleanConverter implements Converter {

	public String toString(Object value) {
		return String.valueOf(value);
	}

	public Boolean fromString(String value) {
		return value != null ? "true".equals(value) : null;
	}

}
