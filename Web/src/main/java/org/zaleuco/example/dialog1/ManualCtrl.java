package org.zaleuco.example.dialog1;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.zaleuco.h2j.filter.DialogueBoost;

@Named
@RequestScoped
public class ManualCtrl implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DIALOGUE_NAME = "dialogue";

	@Inject
	private DialogueBoost dialogueBoost;

	public DatoBean getDato() {
		if (this.dialogueBoost.isInDialogue(DIALOGUE_NAME)) {
			return (DatoBean) this.dialogueBoost.beat(DIALOGUE_NAME);
		} else {
			DatoBean dato = new DatoBean();
			this.dialogueBoost.beat(DIALOGUE_NAME, dato);
			return dato;
		}
	}

	public String submit(String errorPage, String successPage) {
		DatoBean dato;

		dato = (DatoBean) this.dialogueBoost.beat(DIALOGUE_NAME);
		if ("ok".equals(dato.getCognome())) {
			return successPage;
		}

		// IMPORTANTE, se omesso viene creato un nuovo oggetto!
		// omettendolo il dialogo continua ma su un nuovo oggetto e LE PROPRIETA' non
		// passate alla pagina vengono perse
		this.dialogueBoost.replay(DIALOGUE_NAME);

		return errorPage;
	}

}
