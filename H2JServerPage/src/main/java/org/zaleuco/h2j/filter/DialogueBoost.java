package org.zaleuco.h2j.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

@SessionScoped
public class DialogueBoost implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Beat> beats;

	@PostConstruct
	private void init() {
		this.beats = new ArrayList<Beat>();
	}

	/**
	 * 
	 * Avvia un dialogo client server
	 * 
	 * @param dialogue nome del dialogo
	 * @param object   oggetto del dialogo
	 */
	public void beat(String dialogue, Object object) {
		Beat beat = new Beat();
		beat.dialogue = dialogue;
		beat.object = object;
		beat.beat = true;
		this.beats.add(beat);
	}

	public Object beat(String dialogue) {
		for (Beat b : this.beats) {
			if (b.dialogue.equals(dialogue)) {
				return b.object;
			}
		}
		return null;
	}

	/**
	 * 
	 * restituisce true se è in corso un dialogo
	 * 
	 * @param dialogue
	 * @return
	 * @throws IllegalStateException
	 */
	public boolean isDialogue(String dialogue) throws IllegalStateException {
		for (Beat b : this.beats) {
			if (b.dialogue.equals(dialogue)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * Fa si che il dialogo client<->server possa proseguire sul bean
	 * 
	 * @param dialogue nome del dialogo
	 * @return oggetto del dialogo
	 * @throws IllegalStateException
	 */
	public Object replay(String dialogue) throws IllegalStateException {
		for (Beat b : this.beats) {
			if (b.dialogue.equals(dialogue)) {
				b.beat = true;
				return b.object;
			}
		}
		throw new IllegalStateException("unknown dialogue: " + dialogue);
	}

	/**
	 * 
	 * calcella gli oggetti non più di dialogo invocata dopo la response
	 * 
	 */
	void cancel() {
		Beat b;
		int i;
		int n;

		n = this.beats.size();
		for (i = 0; i < n; ++i) {
			b = this.beats.get(i);
			if (!b.beat) {
				this.beats.remove(b);
				--n;
				--i;
			} else {
				b.beat = false;
			}
		}
	}

	public class Beat {
		String dialogue;
		Object object;
		boolean beat;

		public void beat() {
			this.beat = true;
		}

		public Object object() {
			return this.object;
		}
	}
}
