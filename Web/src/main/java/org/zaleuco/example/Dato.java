package org.zaleuco.example;

import java.util.Date;

public class Dato {

	private String nome;
	private String cognome;
	private Date data = new Date();
	private int numero;

	public Dato() {
	}

	public Dato(String nome, String cognome, Date data) {
		this.nome = nome;
		this.cognome = cognome;
		this.data = data;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

}
