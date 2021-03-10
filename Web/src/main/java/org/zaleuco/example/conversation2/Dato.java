package org.zaleuco.example.conversation2;

import java.io.Serializable;

import org.zaleuco.h2j.scope.ConversationRequestScoped;

@ConversationRequestScoped
public class Dato implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id= (int) (Math.random()*1000);
	private String valore11="a";
	private String valore12;
	private String valore21="b";
	private String valore22;
	private String valore31="c";
	private String valore32;
	private String valore41;
	private String valore42;
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValore11() {
		return this.valore11;
	}
	public void setValore11(String valore11) {
		this.valore11 = valore11;
	}
	public String getValore12() {
		return this.valore12;
	}
	public void setValore12(String valore12) {
		this.valore12 = valore12;
	}
	public String getValore21() {
		return this.valore21;
	}
	public void setValore21(String valore21) {
		this.valore21 = valore21;
	}
	public String getValore22() {
		return this.valore22;
	}
	public void setValore22(String valore22) {
		this.valore22 = valore22;
	}
	public String getValore31() {
		return this.valore31;
	}
	public void setValore31(String valore31) {
		this.valore31 = valore31;
	}
	public String getValore32() {
		return this.valore32;
	}
	public void setValore32(String valore32) {
		this.valore32 = valore32;
	}
	public String getValore41() {
		return this.valore41;
	}
	public void setValore41(String valore41) {
		this.valore41 = valore41;
	}
	public String getValore42() {
		return this.valore42;
	}
	public void setValore42(String valore42) {
		this.valore42 = valore42;
	}
	public String toString() {
		return "Dato [id=" + this.id + ", valore11=" + this.valore11 + ", valore12=" + this.valore12 + ", valore21="
				+ this.valore21 + ", valore22=" + this.valore22 + ", valore31=" + this.valore31 + ", valore32="
				+ this.valore32 + ", valore41=" + this.valore41 + ", valore42=" + this.valore42 + "]";
	}

}
