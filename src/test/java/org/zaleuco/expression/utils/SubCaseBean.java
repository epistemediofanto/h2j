package org.zaleuco.expression.utils;

public class SubCaseBean {

	private int subIntero = 13;
	private String subStringa = "ciao";
	private int[] subIntArray = { 1, 2, 3, 4, 5, 6 };
	private String[] subStrArray = { "a", "b", "c" };

	public int subFunInt_1() {
		return 27;
	}

	public int suFunAdd(int a, int b) {
		return a + b;
	}

	public int getSubIntero() {
		return subIntero;
	}

	public void setSubIntero(int subIntero) {
		this.subIntero = subIntero;
	}

	public String getSubStringa() {
		return subStringa;
	}

	public void setSubStringa(String subStringa) {
		this.subStringa = subStringa;
	}

	public int[] getSubIntArray() {
		return subIntArray;
	}

	public void setSubIntArray(int[] subIntArray) {
		this.subIntArray = subIntArray;
	}

	public String[] getSubStrArray() {
		return subStrArray;
	}

	public void setSubStrArray(String[] subStrArray) {
		this.subStrArray = subStrArray;
	}

}
