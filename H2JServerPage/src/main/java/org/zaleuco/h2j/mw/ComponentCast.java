package org.zaleuco.h2j.mw;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;

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
			return Integer.parseInt(value);
		});

		this.castAdapter.put("java.lang.Integer", (String value) -> {
			return Integer.parseInt(value);
		});

		this.castAdapter.put("long", (String value) -> {
			return Long.parseLong(value);
		});

		this.castAdapter.put("java.lang.Long", (String value) -> {
			return Long.parseLong(value);
		});

		this.castAdapter.put("boolean", (String value) -> {
			return Boolean.parseBoolean(value);
		});

		this.castAdapter.put("java.lang.Boolean", (String value) -> {
			return Boolean.parseBoolean(value);
		});

		this.castAdapter.put("java.lang.String", (String value) -> {
			return value;
		});

		this.castAdapter.put("java.util.Calendar", (String value) -> {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			GregorianCalendar gc;
			
			gc = new GregorianCalendar();
			try {
				gc.setTime(sdf.parse(value));
			} catch (ParseException e) {
				gc.setTimeInMillis(0);
			}
			return gc;
		});
		
		this.castAdapter.put("java.util.Date", (String value) -> {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date;
		
			try {
				date = sdf.parse(value);
			} catch (ParseException e) {
				date = new Date(0);
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
	 * @param name
	 * @return
	 */
	public ObjectCastModel get(String name) {
		return this.castAdapter.get(name);
	}
}
