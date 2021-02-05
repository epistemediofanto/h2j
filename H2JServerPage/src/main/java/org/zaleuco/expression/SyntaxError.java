package org.zaleuco.expression;

public class SyntaxError extends Exception {

	private static final long serialVersionUID = 1L;

	public SyntaxError(NodeToken token, String message) {
		super(token.getValue() + " at " + token.getPos() + " - " + message);
	}
}
