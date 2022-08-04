package org.brioscia.javaz.h2j.mw;

import java.io.Serializable;

public class H2JLog implements Serializable {

	private static final long serialVersionUID = 1L;
	public static boolean trace = false;
	public static boolean developmentMode = false;
	public static boolean info = true;
	public static boolean error = true;
	protected static final String TRACE = "h2j.trace";
	protected static final String INFO = "h2j.info";
	protected static final String ERROR = "h2j.error";

	public static void trace(String expFormat, Object... objects) {
		if (trace) {
			String s = String.format(expFormat, objects);
			System.out.println(s);
		}
	}

	public static void debug(String expFormat, Object... objects) {
		if (developmentMode) {
			String s = String.format(expFormat, objects);
			System.out.println(s);
		}
	}

	public static void info(String expFormat, Object... objects) {
		if (info) {
			String s = String.format(expFormat, objects);
			System.out.println(s);
		}
	}

	public static void error(String expFormat, Object... objects) {
		if (error) {
			String s = String.format(expFormat, objects);
			System.out.println(s);
		}
	}

	public static void trace(Throwable throwable, String expFormat, Object... objects) {
		if (trace) {
			String s = String.format(expFormat, objects);
			System.out.println(s);
			throwable.printStackTrace();
		}
	}

	public static void debug(Throwable throwable, String expFormat, Object... objects) {
		if (developmentMode) {
			String s = String.format(expFormat, objects);
			System.out.println(s);
			throwable.printStackTrace();
		}
	}

	public static void info(Throwable throwable, String expFormat, Object... objects) {
		if (info) {
			String s = String.format(expFormat, objects);
			System.out.println(s);
			throwable.printStackTrace();
		}
	}

	public static void error(Throwable throwable, String expFormat, Object... objects) {
		if (error) {
			String s = String.format(expFormat, objects);
			System.out.println(s);
			throwable.printStackTrace();
		}
	}
}
