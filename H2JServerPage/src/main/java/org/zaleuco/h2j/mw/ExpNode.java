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
//	private Object object;

	public ExpNode() {
		this.parameterList = new ArrayList<ExpNode>();
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

	public void setParameterList(List<ExpNode> parameterList) {
		this.parameterList = parameterList;
	}

	public List<ExpNode> getParameterList() {
		return parameterList;
	}

//	public Object getObject() {
//		return object;
//	}
//
//	public void setObject(Object object) {
//		this.object = object;
//	}
}
