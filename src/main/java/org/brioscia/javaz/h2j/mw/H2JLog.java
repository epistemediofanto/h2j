package org.brioscia.javaz.h2j.mw;

import java.io.Serializable;

public class H2JLog implements Serializable {

	private static final long serialVersionUID = 1L;
	protected static boolean trace = false;
	protected static boolean developmentMode = false;
	protected static boolean info = true;
	protected static final String TRACE = "h2j.trace";

	public static void trace(String expFormat, Object... objects) {
		if (trace)
			System.out.println(String.format(expFormat, objects));
	}
	
	public static void debug(String expFormat, Object... objects) {
		if (developmentMode)
			System.out.println(String.format(expFormat, objects));
	}
	
	public static void info(String expFormat, Object... objects) {
		if (info)
			System.out.println(String.format(expFormat, objects));
	}
}
