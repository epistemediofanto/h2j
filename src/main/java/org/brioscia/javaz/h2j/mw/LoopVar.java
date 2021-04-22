package org.brioscia.javaz.h2j.mw;

import org.brioscia.javaz.expression.LexicalParser;
import org.brioscia.javaz.expression.NodeToken;
import org.brioscia.javaz.expression.SyntaxError;
import org.brioscia.javaz.h2j.filter.H2JFilterException;

public class LoopVar {

	private String name;
	private Object object;
	@Deprecated
	private String fullName;
	private NodeToken node;

	public LoopVar(String name, String fullName, Object object) throws H2JFilterException {
		this.name = name;
		this.fullName = fullName;
		this.object = object;
		try {
			this.node = LexicalParser.process(fullName);
		} catch (SyntaxError e) {
			throw new H2JFilterException(e);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public NodeToken getNode() {
		return node;
	}

	public void setNode(NodeToken node) {
		this.node = node;
	}

}
