package org.zaleuco.expression;

import java.util.ArrayList;
import java.util.List;

public class NodeToken {

	public enum Type {
		nil, name, function, number, string, arrayIndex, dot, add, sub, mul, div, pow, not, and, or, lt, let, gt, get, eq, neq, neg, undef;
	}

	private String value;
	private int pos;
	private Type type;
	private List<NodeToken> childs = new ArrayList<NodeToken>();

	public void resetChild() {
		this.childs.clear();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<NodeToken> getChilds() {
		return childs;
	}

	public void setChilds(List<NodeToken> childs) {
		this.childs = childs;
	}

	public void add(NodeToken token) {
		this.childs.add(token);
	}

	public String toString() {
		String out = null;
		Type type = this.type != null ? this.type : Type.undef;
		switch (type) {
		case add:
			out = this.getChilds().get(0) + " + " + this.getChilds().get(1);
			break;
		case and:
			out = this.getChilds().get(0) + " && " + this.getChilds().get(1);
			break;
		case arrayIndex:
			break;
		case div:
			out = this.getChilds().get(0) + " / " + this.getChilds().get(1);
			break;
		case dot:
			break;
		case eq:
			out = this.getChilds().get(0) + " == " + this.getChilds().get(1);
			break;
		case function:
			break;
		case get:
			out = this.getChilds().get(0) + " >= " + this.getChilds().get(1);
			break;
		case gt:
			out = this.getChilds().get(0) + " > " + this.getChilds().get(1);
			break;
		case let:
			out = this.getChilds().get(0) + " <= " + this.getChilds().get(1);
			break;
		case lt:
			out = this.getChilds().get(0) + " < " + this.getChilds().get(1);
			break;
		case mul:
			out = this.getChilds().get(0) + " * " + this.getChilds().get(1);
			break;
		case name:
			out = this.value;
			break;
		case neg:
			out = "-" + this.getChilds().get(0);
			break;
		case neq:
			out = this.getChilds().get(0) + " != " + this.getChilds().get(1);
			break;
		case nil:
			break;
		case not:
			out = "!" + this.getChilds().get(0);
			break;
		case number:
			out = this.value;
			break;
		case or:
			out = this.getChilds().get(0) + " !!& " + this.getChilds().get(1);
			break;
		case pow:
			out = this.getChilds().get(0) + " ^ " + this.getChilds().get(1);
			break;
		case string:
			out = "'" + this.value + "'";
			break;
		case sub:
			out = this.getChilds().get(0) + " - " + this.getChilds().get(1);
			break;
		case undef:
			out = "???";
			break;
		}

		int childPos = 0;
		boolean f;
		if ((this.childs.size() > childPos) && (this.childs.get(childPos).getType() == Type.function)) {
			out += "(";
			f = false;
			for (NodeToken n : this.childs.get(childPos).getChilds()) {
				out += (f ? ", " : "") + n;
				f = true;
			}
			out += ")";
			++childPos;
		}
		while ((this.childs.size() > childPos) && (this.childs.get(childPos).getType() == Type.arrayIndex)) {
			out += "[";
			f = false;
			for (NodeToken n : this.childs.get(childPos).getChilds()) {
				out += (f ? ", " : "") + n;
				f = true;
			}
			out += "]";
			++childPos;
		}
		if ((this.childs.size() > childPos) && (this.childs.get(childPos).getType() == Type.dot)) {
			out += "." + this.childs.get(childPos).getChilds().get(0);
			++childPos;
		}
		return out;
	}
}
