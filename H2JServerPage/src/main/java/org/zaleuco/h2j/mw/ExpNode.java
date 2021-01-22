package org.zaleuco.h2j.mw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExpNode implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Type {
		Property, FunctionCall
	}

	private String objectName;
	private List<ExpNode> parameterList;
	private ExpNode property;
	private Type type;
	private List<ExpNode> arrayIndex;

	public ExpNode() {
		this.parameterList = new ArrayList<ExpNode>();
		this.arrayIndex = new ArrayList<ExpNode>();
	}

	public ExpNode(String objectName, Type type) {
		this();
		this.type = type;
		this.objectName = objectName;
	}

	public String toString() {
		String out;
		out = this.objectName;
		if (this.type == Type.FunctionCall) {
			out += "(";
			for (int i = 0; i < this.parameterList.size(); ++i) {
				out += (i == 0 ? "" : ", ") + this.parameterList.get(i);
			}
			out += ")";
		}
		for (int i = 0; i < this.arrayIndex.size(); ++i) {
			out += "[" + this.arrayIndex.get(i) + "]";
		}
		if (this.property != null) {
			out += "." + this.property;
		}
		return out;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public ExpNode getProperty() {
		return property;
	}

	public void setProperty(ExpNode property) {
		this.property = property;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void addParameter(ExpNode node) {
		this.parameterList.add(node);
	}

	public List<ExpNode> getParameterList() {
		return parameterList;
	}

	public void addArrayIndex(ExpNode node) {
		this.arrayIndex.add(node);
	}

	public List<ExpNode> getArrayIndex() {
		return arrayIndex;
	}
}
