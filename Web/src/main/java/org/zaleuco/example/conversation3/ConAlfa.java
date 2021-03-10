package org.zaleuco.example.conversation3;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.zaleuco.h2j.scope.ConversationRequest;

@Named
@RequestScoped
public class ConAlfa {

	@Inject
	private ConversationRequest conversation;
	
	private int id;
	
	@Inject
	private Dato dato;
	
	public ConAlfa() {
		this.id = (int) (Math.random()*1000);
	}
	
	public String submit(String pagina) {
		System.out.println(this.dato);
		return pagina;
	}

	public String nuovo(String pagina) {
		System.out.println(this.dato);
		this.conversation.end(Dato.class);
		return pagina;
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Dato getDato() {
		return this.dato;
	}

	public void setDato(Dato dato) {
		this.dato = dato;
	}
}
