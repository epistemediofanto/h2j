package org.zaleuco.h2j.filter.cast;

public interface Converter {

	public String toString(Object value);
	public Object fromString(String value);
	
}
