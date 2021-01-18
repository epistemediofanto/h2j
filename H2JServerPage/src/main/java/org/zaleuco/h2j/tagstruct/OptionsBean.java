package org.zaleuco.h2j.tagstruct;

public class OptionsBean implements Options {

	private static final long serialVersionUID = 1L;

	private String label;
	private String value;

	public OptionsBean() {
	}

	public OptionsBean(String label, String value) {
		this.setLabel(label);
		this.setValue(value);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
