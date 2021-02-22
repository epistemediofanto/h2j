package org.zaleuco.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class OggettoForm {

	private String nome;
	private String cognome = "Rossi";
	private String link = "target.xhtml";
	private Date oggi = new Date();
	private int numero = 1999;
	private double reale = 1.25;
	private List<Dato> dati;

	public OggettoForm() {
		this.dati = new ArrayList<Dato>();
		this.dati.add(new Dato("Maria", "Sany", new Date()));
		this.dati.add(new Dato("Anna", "Maria", new Date()));
	}

	public String addDato(Dato d) {
		this.dati.add(d);
		return "target.xhtml";
	}

	public String trasmetti(String p) {
		return p;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String submit(String errpage, String nextpage) {
		return nextpage;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public List<Dato> getDati() {
		return dati;
	}

	public void setDati(List<Dato> dati) {
		this.dati = dati;
	}

	public Date getOggi() {
		return this.oggi;
	}

	public void setOggi(Date oggi) {
		this.oggi = oggi;
	}

	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public double getReale() {
		return this.reale;
	}

	public void setReale(double reale) {
		this.reale = reale;
	}

	public String invia(Integer numero, String testo, String nome) {
		this.numero = numero;
		return this.link;
	}
}
