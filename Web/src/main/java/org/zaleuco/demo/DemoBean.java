package org.zaleuco.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.zaleuco.h2j.tagstruct.Options;
import org.zaleuco.h2j.tagstruct.OptionsBean;

@Named(value = "demo")
@RequestScoped
public class DemoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nome;
	private int numero;
	private String indirizzo;
	private AltroBean altroBean = new AltroBean("Roma@it.it", "RM", 1);
	private List<AltroBean> lista = new ArrayList<AltroBean>();

	public DemoBean() {
		lista.add(new AltroBean("ROMA", "RM", 19));
		lista.add(new AltroBean("MILANO", "MI", 24));
		lista.add(new AltroBean("REGGIO", "RC", 31));
		lista.add(new AltroBean("BOVA", "RC", 42));
		lista.add(new AltroBean("MELITO", "RC", 53));
	}

	public AltroBean getAltroBean() {
		return altroBean;
	}

	public void setAltroBean(AltroBean altroBean) {
		this.altroBean = altroBean;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Options> getLista() {
		List<Options> lista;
		lista = new ArrayList<Options>();

		lista.add(new OptionsBean("label1", "valore1"));
		lista.add(new OptionsBean("label2", "valore2"));
		lista.add(new OptionsBean("label3", "valore3"));

		return lista;
	}

	public List<AltroBean> getListaAltro() {
		return lista;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		if (indirizzo != null) {
			this.lista.add(new AltroBean(indirizzo, indirizzo, 19));
		}
		this.indirizzo = indirizzo;
	}

	@Override
	public String toString() {
		return "DemoBean [nome=" + nome + ", numero=" + numero + ", indirizzo=" + indirizzo + ", altroBean=" + altroBean
				+ ", lista=" + lista + "]";
	}
}
