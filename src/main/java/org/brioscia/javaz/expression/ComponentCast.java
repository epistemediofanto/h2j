package org.brioscia.javaz.expression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.brioscia.javaz.h2j.filter.H2JProcessorFilter;

public class ComponentCast {

	private Logger Log = Logger.getLogger(ComponentCast.class.getName());
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
			Integer ret = null;
			if ((avalue != null) && (avalue[0] != null)) {
				String value = avalue[0].toString();
				ret = (value.length() > 0) ? Integer.parseInt(value) : 0;
			}
			return ret;
		});

		this.castAdapter.put("int[]", (Object[] value) -> {
			int[] newValue = null;
			if (value != null) {
				int len = value.length;
				newValue = new int[len];
				for (int i = 0; i < len; ++i) {
					newValue[i] = Integer.parseInt(value[i].toString());
				}
			}
			return newValue;
		});

		this.castAdapter.put("java.lang.Integer", (Object[] avalue) -> {
			Integer ret = null;
			if ((avalue != null) && (avalue[0] != null)) {
				String value = avalue[0].toString();
				ret = (value.length() > 0) ? Integer.parseInt(value) : null;
			}
			return ret;
		});

		this.castAdapter.put("java.lang.Integer[]", (Object[] value) -> {
			Integer newValue[] = null;
			if (value != null) {
				int len = value.length;
				newValue = new Integer[len];
				for (int i = 0; i < len; ++i) {
					newValue[i] = Integer.parseInt(value[i].toString());
				}
			}
			return newValue;
		});

		this.castAdapter.put("long", (Object[] avalue) -> {
			Long ret = null;
			if ((avalue != null) && (avalue[0] != null)) {
				String value = avalue[0].toString();
				ret = (value.length() > 0) ? Long.parseLong(value) : 0;
			}
			return ret;
		});

		this.castAdapter.put("long[]", (Object[] value) -> {
			long[] newValue = null;
			if (value != null) {
				int len = value.length;
				newValue = new long[len];
				for (int i = 0; i < len; ++i) {
					newValue[i] = Long.parseLong(value[i].toString());
				}
			}
			return newValue;
		});

		this.castAdapter.put("java.lang.Long", (Object[] avalue) -> {
			Long ret = null;
			if ((avalue != null) && (avalue[0] != null)) {
				String value = avalue[0].toString();
				ret = (value.length() > 0) ? Long.parseLong(value) : null;
			}
			return ret;
		});

		this.castAdapter.put("java.lang.Long[]", (Object[] value) -> {
			Long[] newValue = null;
			if (value != null) {
				int len = value.length;
				newValue = new Long[len];
				for (int i = 0; i < len; ++i) {
					newValue[i] = Long.parseLong(value[i].toString());
				}
			}
			return newValue;
		});

		this.castAdapter.put("boolean", (Object[] avalue) -> {
			Boolean ret = null;
			if ((avalue != null) && (avalue[0] != null)) {
				String value = avalue[0].toString();
				ret = (value.length() > 0) ? Boolean.parseBoolean(value) : false;
			}
			return ret;
		});

		this.castAdapter.put("boolean[]", (Object[] value) -> {
			Boolean[] newValue = null;
			if (value != null) {
				int len = value.length;
				newValue = new Boolean[len];
				for (int i = 0; i < len; ++i) {
					newValue[i] = Boolean.parseBoolean(value[i].toString());
				}
			}
			return newValue;
		});

		this.castAdapter.put("java.lang.Boolean", (Object[] avalue) -> {
			Boolean ret = null;
			if ((avalue != null) && (avalue[0] != null)) {
				String value = avalue[0].toString();
				ret = (value.length() > 0) ? Boolean.parseBoolean(value) : null;
			}
			return ret;
		});

		this.castAdapter.put("java.lang.Boolean[]", (Object[] value) -> {
			Boolean[] newValue = null;
			if (value != null) {
				int len = value.length;
				newValue = new Boolean[len];
				for (int i = 0; i < len; ++i) {
					newValue[i] = Boolean.parseBoolean(value[i].toString());
				}
			}
			return newValue;
		});

		this.castAdapter.put("java.lang.String", (Object[] avalue) -> {
			String ret = null;
			if ((avalue != null) && (avalue[0] != null)) {
				ret = avalue[0].toString();
			}
			return ret;
		});

		this.castAdapter.put("java.lang.String[]", (Object[] value) -> {
			String[] newValue = null;
			if (value != null) {
				int len = value.length;
				newValue = new String[len];
				for (int i = 0; i < len; ++i) {
					newValue[i] = (String) value[i];
				}
			}
			return newValue;
		});

		this.castAdapter.put("java.util.Calendar", (Object[] avalue) -> {
			Calendar ret = null;
			if ((avalue != null) && (avalue[0] != null)) {
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
				ret = gc;
			}
			return ret;
		});

		this.castAdapter.put("java.util.Date", (Object[] avalue) -> {
			Date ret = null;

			if ((avalue != null) && (avalue[0] != null)) {
				if (avalue[0] instanceof Date) {
					ret = (Date) avalue[0];
				} else {
					String value = avalue[0].toString();
					if ((value != null) && (value.length() > 0)) {
						SimpleDateFormat sdf = new SimpleDateFormat(H2JProcessorFilter.DATE_ISO_8601);
						try {
							ret = sdf.parse(value);
						} catch (ParseException e) {
							Log.log(Level.SEVERE, e.getMessage(), e);
							throw new SyntaxError(null, "invalid date format " + value + " valid format is "
									+ H2JProcessorFilter.DATE_ISO_8601);
						}
					}
				}
			}
			return ret;
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
