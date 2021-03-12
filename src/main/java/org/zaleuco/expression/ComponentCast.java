package org.zaleuco.expression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.zaleuco.h2j.filter.cast.Shape;

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

		this.castAdapter.put("int", (String value) -> {
			return (value != null) && (value.length() > 0) ? Integer.parseInt(value) : 0;
		});

		this.castAdapter.put("java.lang.Integer", (String value) -> {
			return (value != null) && (value.length() > 0) ? Integer.parseInt(value) : null;
		});

		this.castAdapter.put("long", (String value) -> {
			return (value != null) && (value.length() > 0) ? Long.parseLong(value) : 0;
		});

		this.castAdapter.put("java.lang.Long", (String value) -> {
			return (value != null) && (value.length() > 0) ? Long.parseLong(value) : null;
		});

		this.castAdapter.put("boolean", (String value) -> {
			return (value != null) && (value.length() > 0) ? Boolean.parseBoolean(value) : false;
		});

		this.castAdapter.put("java.lang.Boolean", (String value) -> {
			return (value != null) && (value.length() > 0) ? Boolean.parseBoolean(value) : null;
		});

		this.castAdapter.put("java.lang.String", (String value) -> {
			return value;
		});

		this.castAdapter.put("java.util.Calendar", (String value) -> {
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

		this.castAdapter.put("java.util.Date", (String value) -> {
			Date date = null;
			if ((value != null) && (value.length() > 0)) {
				SimpleDateFormat sdf = new SimpleDateFormat(Shape.ISO_8601);
				try {
					date = sdf.parse(value);
				} catch (ParseException e) {
					throw new SyntaxError(null, value);
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
