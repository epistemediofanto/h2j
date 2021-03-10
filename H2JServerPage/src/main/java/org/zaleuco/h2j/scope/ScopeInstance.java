package org.zaleuco.h2j.scope;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

class ScopeInstance<T> {
	Bean<T> bean;
	CreationalContext<T> ctx;
	T originalBean;
}
