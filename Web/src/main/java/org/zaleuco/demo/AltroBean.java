package org.zaleuco.demo;

import java.io.Serializable;

public class AltroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String comune;
	private String provincia;
	private int numero;

	public AltroBean(String comune, String provincia, int numero) {
		this.comune = comune;
		this.provincia = provincia;
		this.numero = numero;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return "AltroBean [comune=" + comune + ", provincia=" + provincia + ", numero=" + numero + "]";
	}

}
