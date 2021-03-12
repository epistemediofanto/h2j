package org.zaleuco.h2j.filter.cast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		if ((value != null) && (value.length() > 0)) {
			SimpleDateFormat sdf = new SimpleDateFormat(this.format);
			try {
				date = sdf.parse(value);
			} catch (ParseException e) {
				Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return date;
	}

}
