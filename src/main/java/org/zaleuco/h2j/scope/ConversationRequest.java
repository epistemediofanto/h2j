package org.zaleuco.h2j.scope;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class ConversationRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, ScopeInstance<?>> beans;

	public ConversationRequest() {
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
	 * Trermina la conversazione per tutti gli oggetti di clase "clazz"
	 * 
	 * @param clazz chiude la conversazione sulle istanze di clazz
	 */
	public void end(Class<?> clazz) {
		this.destroyBean(this.getBean(clazz));
	}

	/**
	 * Termina tutte le conversazioni aperte
	 */
	public void end() {
		Set<Entry<String, ScopeInstance<?>>> set = this.beans.entrySet();
		set.forEach(e -> {
			this.destroyBean(e.getValue());
		});
	}

}