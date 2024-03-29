package org.brioscia.javaz.h2j.scope;

import java.io.Serializable;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class DialogExtension implements Extension, Serializable {

	private static final long serialVersionUID = 1L;

	public void addScope(@Observes final BeforeBeanDiscovery event) {
		event.addScope(DialogScoped.class, true, false);
	}

	public void registerContext(@Observes final AfterBeanDiscovery event) {
		event.addContext(new DialogRegisterContext());
	}

}
