package org.zaleuco.example;

public class Coppia {
	private String descrizione;
	private String valore;

	public Coppia() {

	}

	public Coppia(String valore, String descrizione) {
		this.valore = valore;
		this.descrizione = descrizione;
	}

	public String getValore() {
		return this.valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
