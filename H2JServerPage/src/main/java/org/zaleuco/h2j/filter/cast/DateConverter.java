package org.zaleuco.h2j.filter.cast;

import java.lang.System.Logger.Level;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter {

	private String format;

	public DateConverter(String format) {
		this.format = format;
	}

	public String toString(Object value) {
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(this.format);
			return sdf.format((Date) value);
		} else {
			return "";
		}
	}

	public Date fromString(String value) {
		Date date = null;
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(this.format);
			try {
				date = sdf.parse(value);
			} catch (ParseException e) {
				System.getLogger("converter").log(Level.ERROR, e);
			}
		}
		return date;
	}

}
