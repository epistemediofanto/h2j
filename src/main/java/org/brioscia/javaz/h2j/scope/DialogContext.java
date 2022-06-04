package org.brioscia.javaz.h2j.scope;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class DialogContext implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, ScopeInstance<?>> beans;

	public DialogContext() {
		this.beans = Collections.synchronizedMap(new HashMap<String, ScopeInstance<?>>());
	}

	public Map<String, ScopeInstance<?>> getBeans() {
		return this.beans;
	}

	public ScopeInstance<?> getBean(Class<?> beanClass) {
		return this.getBeans().get(beanClass.getName());
	}

	public void putBean(ScopeInstance<?> scopeInstance) {
		this.getBeans().put(scopeInstance.originalBean.getClass().getName(), scopeInstance);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void destroyBean(ScopeInstance scopeInstance) {
		this.getBeans().remove(scopeInstance.bean.getBeanClass().getName());
		scopeInstance.bean.destroy(scopeInstance.originalBean, scopeInstance.ctx);
	}

	/**
	 * Rimuove l'oggetto dallo scope
	 * 
	 * @param objectBean oggetto da rimuovere
	 */
	public void end(Object objectBean) {
		Set<Entry<String, ScopeInstance<?>>> set = this.beans.entrySet();
		set.forEach(e -> {
			if (e.getValue().originalBean != objectBean) {
				this.beans.remove(e.getKey());
				return;
			}
		});
	}

}