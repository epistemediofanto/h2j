package org.zaleuco.expression;

public class InvokerException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvokerException(NodeToken token, Exception e) {
		super(token.getValue() + " at " + token.getPos(), e);
	}

	public InvokerException(NodeToken token, String message) {
		super(token.getValue() + " at " + token.getPos() + " - " + message);
	}
}
