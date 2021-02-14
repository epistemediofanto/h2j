package org.zaleuco.example.dialog2;

public class AutoDatoBean {

	private Integer numero;
	private String nome = "Sany";
	private String cognome;

	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String submit(String errorPage, String successPage) {
		if ("ok".equals(this.cognome)) {
			return successPage;
		}
		return errorPage;
	}
}
