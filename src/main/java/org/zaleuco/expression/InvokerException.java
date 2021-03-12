package org.zaleuco.expression;

public class InvokerException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvokerException(String message) {
		super(message);
	}
	
	public InvokerException(NodeToken token, Exception e) {
		super(token.getValue() + " at pos " + token.getPos(), e);
	}

	public InvokerException(NodeToken token, String message) {
		super(token.getValue() + " at pos " + token.getPos() + " - " + message);
	}
}
