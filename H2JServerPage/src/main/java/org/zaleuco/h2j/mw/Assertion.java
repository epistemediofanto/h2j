package org.zaleuco.h2j.mw;

import java.io.Serializable;

import org.zaleuco.h2j.filter.H2JFilterException;

public class Assertion implements Serializable {

	private static final long serialVersionUID = 1L;

	public void assertNotNull(Object o, String msg) throws H2JFilterException {
		if (o == null) {
			throw new H2JFilterException(msg);
		}
	}

	public void assertTrue(boolean exp, String msg) throws H2JFilterException {
		if (!exp) {
			throw new H2JFilterException(msg);
		}
	}

	public void assertTrue(boolean exp, String format, Object...args) throws H2JFilterException {
		if (!exp) {
			throw new H2JFilterException(String.format(format, args));
		}
	}
}
