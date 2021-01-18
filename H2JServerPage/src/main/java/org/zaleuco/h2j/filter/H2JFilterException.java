package org.zaleuco.h2j.filter;

public class H2JFilterException extends Exception {

	private static final long serialVersionUID = 1L;

	public H2JFilterException(String msg) {
		super(msg);
	}
	
	public H2JFilterException(Exception e) {
		super(e);
	}
	
	public H2JFilterException(String msg, Exception e) {
		super(msg, e);
	}
}
