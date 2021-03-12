package org.zaleuco.h2j.scope;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.logging.Logger;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

public class ConversationRequestScopeContext implements Context, Serializable {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private ConversationRequest conversationRequest;

	public ConversationRequestScopeContext() {
		this.conversationRequest = javax.enterprise.inject.spi.CDI.current().select(ConversationRequest.class).get();
	}

	@SuppressWarnings("unchecked")
	public <T> T get(final Contextual<T> contextual) {
		Bean<T> bean = (Bean<T>) contextual;
		T instanceBean = null;
		ScopeInstance<?> scopeInstance = this.conversationRequest.getBean(bean.getBeanClass());
		if (scopeInstance != null) {
			instanceBean = (T) scopeInstance.originalBean;
		} else {
			log.fine("bean not in scope: " + bean.getName());
		}
		return instanceBean;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(final Contextual<T> contextual, final CreationalContext<T> creationalContext) {
		Bean<T> bean = (Bean<T>) contextual;
		T instanceBean;
		ScopeInstance<?> scopeInstance = this.conversationRequest.getBean(bean.getBeanClass());
		if (scopeInstance != null) {
			instanceBean = (T) scopeInstance.originalBean;
		} else {
			instanceBean = (T) bean.create(creationalContext);
			ScopeInstance<T> instanceScope = new ScopeInstance<T>();
			instanceScope.bean = bean;
			instanceScope.ctx = creationalContext;
			instanceScope.originalBean = instanceBean;
			this.conversationRequest.putBean(instanceScope);
		}
		return instanceBean;
	}

	public Class<? extends Annotation> getScope() {
		return ConversationRequestScoped.class;
	}

	public boolean isActive() {
		return true;
	}

}