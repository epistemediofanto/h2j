package org.zaleuco.h2j.scope;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

	public void end(Class<?> cls) {
		this.destroyBean(this.getBean(cls));
	}
	
	public void endAll() {
		this.beans.forEach((k,v)->{
			this.destroyBean(v);
		});
	}

}