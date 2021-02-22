package org.zaleuco.example;

import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class OggettoRMI {

	private String jsonObject;

	public Dato oggettoDato() {
		return new Dato("Maria", "Sany", new Date());
	}

	public String valida() {
		return jsonObject;
	}

	public String getJsonObject() {
		return this.jsonObject;
	}

	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}

	public Coppia[] select(String value) {
		Coppia[] c = new Coppia[] { new Coppia("a", value + " Lettera a"), new Coppia("b", value + " Lettera b"),
				new Coppia("c", value + " Lettera c") };
		return c;
	}
}
