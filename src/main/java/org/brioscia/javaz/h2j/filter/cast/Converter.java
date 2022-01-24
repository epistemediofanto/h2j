package org.brioscia.javaz.h2j.filter.cast;

public interface Converter {

	public String toString(Object value);
	public Object fromString(String value);
	public String getName();
}
