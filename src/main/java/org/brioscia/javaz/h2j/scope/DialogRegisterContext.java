package org.brioscia.javaz.h2j.scope;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.brioscia.javaz.h2j.mw.H2JLog;

public class DialogRegisterContext implements Context, Serializable {

	private static final long serialVersionUID = 1L;

	private DialogContext dialogContext;

	public DialogRegisterContext() {
		this.dialogContext = javax.enterprise.inject.spi.CDI.current().select(DialogContext.class).get();
	}

	@SuppressWarnings("unchecked")
	public <T> T get(final Contextual<T> contextual) {
		Bean<T> bean = (Bean<T>) contextual;
		T instanceBean = null;
		ScopeInstance<?> scopeInstance = this.dialogContext.getBean(bean.getBeanClass());
		if (scopeInstance != null) {
			instanceBean = (T) scopeInstance.originalBean;
		} else {
			H2JLog.trace("bean not in scope: " + bean.getName());
		}
		return instanceBean;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(final Contextual<T> contextual, final CreationalContext<T> creationalContext) {
		Bean<T> bean = (Bean<T>) contextual;
		T instanceBean;
		ScopeInstance<?> scopeInstance = this.dialogContext.getBean(bean.getBeanClass());
		if (scopeInstance != null) {
			instanceBean = (T) scopeInstance.originalBean;
		} else {
			instanceBean = (T) bean.create(creationalContext);
			ScopeInstance<T> instanceScope = new ScopeInstance<T>();
			instanceScope.bean = bean;
			instanceScope.ctx = creationalContext;
			instanceScope.originalBean = instanceBean;
			this.dialogContext.putBean(instanceScope);
		}
		return instanceBean;
	}

	public Class<? extends Annotation> getScope() {
		return DialogScoped.class;
	}

	public boolean isActive() {
		return true;
	}

}