package org.zaleuco.example.dialog2;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.zaleuco.h2j.filter.DialogueBoost;

@Named
@RequestScoped
public class AutoCtrl implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DIALOGUE_NAME = "a_dialogue";

	@Inject
	private DialogueBoost dialogueBoost;

	public String start() {
		AutoDatoBean dato = new AutoDatoBean();
		this.dialogueBoost.beat(DIALOGUE_NAME, dato);
		return "form.xhtml";
	}

}
