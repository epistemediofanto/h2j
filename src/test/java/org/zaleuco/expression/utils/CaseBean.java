package org.zaleuco.expression.utils;

import java.util.Date;

public class CaseBean {

	private int intero = 13;
	private String stringa = "ciao";
	private int[] intArray = { 1, 2, 3, 4, 5, 6 };
	private int[][] intIntArray = { { 2, 3 }, { 4, 5 } };
	private double[] doubleArray = { 1, 2, 3, 4, 5, 6 };
	private long[] longArray = { 1, 2, 3, 4, 5, 6 };
	private float[] floatArray = { 1, 2, 3, 4, 5, 6 };
	private byte[] byteArray = { 1, 2, 3, 4, 5, 6 };
	private boolean[] boolArray = { true, true, false };
	private char[] charArray = { 'a', 'b', 'c', 'd' };
	private String[] strArray = { "a", "b", "c" };
	private SubCaseBean subCaseBean = new SubCaseBean();
	private int uno = 1;
	private int due = 2;
	private Date data=new Date(30, 7, 11);

	public int funInt_1() {
		return 27;
	}

	public int funAdd(int a, int b) {
		return a + b;
	}

	public int getIntero() {
		return intero;
	}

	public void setIntero(int intero) {
		this.intero = intero;
	}

	public String getStringa() {
		return stringa;
	}

	public void setStringa(String stringa) {
		this.stringa = stringa;
	}

	public int[] getIntArray() {
		return intArray;
	}

	public void setIntArray(int[] intArray) {
		this.intArray = intArray;
	}

	public String[] getStrArray() {
		return strArray;
	}

	public void setStrArray(String[] strArray) {
		this.strArray = strArray;
	}

	public double[] getDoubleArray() {
		return doubleArray;
	}

	public void setDoubleArray(double[] doubleArray) {
		this.doubleArray = doubleArray;
	}

	public long[] getLongArray() {
		return longArray;
	}

	public void setLongArray(long[] longArray) {
		this.longArray = longArray;
	}

	public float[] getFloatArray() {
		return floatArray;
	}

	public void setFloatArray(float[] floatArray) {
		this.floatArray = floatArray;
	}

	public boolean[] getBoolArray() {
		return boolArray;
	}

	public void setBoolArray(boolean[] boolArray) {
		this.boolArray = boolArray;
	}

	public char[] getCharArray() {
		return charArray;
	}

	public void setCharArray(char[] charArray) {
		this.charArray = charArray;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	public int[][] getIntIntArray() {
		return intIntArray;
	}

	public void setIntIntArray(int[][] intIntArray) {
		this.intIntArray = intIntArray;
	}

	public SubCaseBean getSubCaseBean() {
		return subCaseBean;
	}

	public void setSubCaseBean(SubCaseBean subCaseBean) {
		this.subCaseBean = subCaseBean;
	}

	public int getUno() {
		return uno;
	}

	public void setUno(int uno) {
		this.uno = uno;
	}

	public int getDue() {
		return due;
	}

	public void setDue(int due) {
		this.due = due;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}
