package org.zaleuco.example.longreq;

import javax.inject.Inject;
import javax.inject.Named;

import org.zaleuco.scope.LongRequestContext;
import org.zaleuco.scope.LongRequestScoped;

@Named
@LongRequestScoped
public class LongCtrl {

	private int id = (int) (Math.random() * 1000);
	private String nome;
	private String cognome;
	private Integer numero;
	private String indirizzo;
	private String localita;
	
//	@Inject
//	private LongRequestContext context;

	public String submit(String forwardPage) {
		return forwardPage;
	}

	public String newBean(String forwardPage) {
//		this.killEvent.fire(new H2JKillEvent(this.getClass()));
//		this.context.destroy(LongCtrl.class);
		return forwardPage;
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

	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getLocalita() {
		return this.localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
