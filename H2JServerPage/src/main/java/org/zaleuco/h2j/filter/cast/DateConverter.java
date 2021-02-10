package org.zaleuco.h2j.filter.cast;

import java.lang.System.Logger.Level;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter {

	public String toString(Object value) {
		return String.format("%1$td/%1$tm/%1$ty", value);
	}

	public Date fromString(String value) {
		Date date = null;
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				date = sdf.parse(value);
			} catch (ParseException e) {
				System.getLogger("converter").log(Level.ERROR, e);
			}
		}
		return date;
	}

}
