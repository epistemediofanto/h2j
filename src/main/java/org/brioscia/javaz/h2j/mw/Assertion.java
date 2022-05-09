package org.brioscia.javaz.h2j.mw;

import org.brioscia.javaz.h2j.filter.H2JFilterException;

public class Assertion extends H2JLog {

	private static final long serialVersionUID = 1L;

	protected void assertNotNull(Object o, String msg) throws H2JFilterException {
		if (o == null) {
			throw new H2JFilterException(msg);
		}
	}

	protected void assertTrue(boolean exp, String msg) throws H2JFilterException {
		if (!exp) {
			throw new H2JFilterException(msg);
		}
	}

	protected void assertTrue(boolean exp, String format, Object... args) throws H2JFilterException {
		if (!exp) {
			throw new H2JFilterException(String.format(format, args));
		}
	}
}
