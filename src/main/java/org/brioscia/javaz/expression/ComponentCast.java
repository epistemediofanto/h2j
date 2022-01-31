package org.brioscia.javaz.expression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.brioscia.javaz.h2j.filter.cast.Shape;

public class ComponentCast {

	private HashMap<String, ObjectCastModel> castAdapter;

	public ComponentCast() {
		this.reset();
	}

	/**
	 * 
	 * Restore default component cast
	 * 
	 */
	public void reset() {
		this.castAdapter = new HashMap<String, ObjectCastModel>();

		this.castAdapter.put("int", (Object[] avalue) -> {
			String value = avalue[0].toString();
			return (value != null) && (value.length() > 0) ? Integer.parseInt(value) : 0;
		});

		this.castAdapter.put("java.lang.Integer", (Object[] avalue) -> {
			String value = avalue[0].toString();
			return (value != null) && (value.length() > 0) ? Integer.parseInt(value) : null;
		});

		this.castAdapter.put("long", (Object[] avalue) -> {
			String value = avalue[0].toString();
			return (value != null) && (value.length() > 0) ? Long.parseLong(value) : 0;
		});

		this.castAdapter.put("java.lang.Long", (Object[] avalue) -> {
			String value = avalue[0].toString();
			return (value != null) && (value.length() > 0) ? Long.parseLong(value) : null;
		});

		this.castAdapter.put("boolean", (Object[] avalue) -> {
			String value = avalue[0].toString();
			return (value != null) && (value.length() > 0) ? Boolean.parseBoolean(value) : false;
		});

		this.castAdapter.put("java.lang.Boolean", (Object[] avalue) -> {
			String value = avalue[0].toString();
			return (value != null) && (value.length() > 0) ? Boolean.parseBoolean(value) : null;
		});

		this.castAdapter.put("java.lang.String", (Object[] avalue) -> {
			String value = avalue[0].toString();
			return value;
		});

		this.castAdapter.put("java.lang.String[]", (Object[] value) -> {
			int len = value.length;
			String[] newValue = new String[len];
			for (int i=0; i<len; ++i) {
				newValue[i] = value[i].toString();
			}
			return newValue;
		});

		this.castAdapter.put("java.util.Calendar", (Object[] avalue) -> {
			String value = avalue[0].toString();
			GregorianCalendar gc = null;
			if ((value != null) && (value.length() > 0)) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

				gc = new GregorianCalendar();
				try {
					gc.setTime(sdf.parse(value));
				} catch (ParseException e) {
					gc.setTimeInMillis(0);
				}
			}
			return gc;
		});

		this.castAdapter.put("java.util.Date", (Object[] avalue) -> {
			String value = avalue[0].toString();
			Date date = null;
			if ((value != null) && (value.length() > 0)) {
				SimpleDateFormat sdf = new SimpleDateFormat(Shape.ISO_8601);
				try {
					date = sdf.parse(value);
				} catch (ParseException e) {
					throw new SyntaxError(null, "invalid date format " + value + " valid format are " + Shape.ISO_8601);
				}
			}
			return date;
		});
	}

	/**
	 * 
	 * adds a type converter
	 * 
	 * @param typeName type name
	 * @param cast     object cast model
	 */
	public void registerComponentCast(String typeName, ObjectCastModel cast) {
		this.castAdapter.put(typeName, cast);
	}

	/**
	 * 
	 * remove a type converter
	 * 
	 * @param typeName name of converter
	 */
	public void unregisterComponentCast(String typeName) {
		this.castAdapter.remove(typeName);
	}

	/**
	 * 
	 * Return a object cast model
	 * 
	 * @param name nome del tipo
	 * @return oggetto da utilizzare per il cast del valore
	 */
	public ObjectCastModel get(String name) {
		return this.castAdapter.get(name);
	}
}
