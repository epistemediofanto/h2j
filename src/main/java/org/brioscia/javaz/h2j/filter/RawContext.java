package org.brioscia.javaz.h2j.filter;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Named
@SessionScoped
public class RawContext implements Serializable {

	private static final long serialVersionUID = 1L;
	private ServletRequest request;
	private ServletResponse response;
	private boolean directResponse = false;

	void set(ServletRequest request, ServletResponse response) {
		this.request = request;
		this.response = response;
		this.directResponse = false;
	}

	public ServletRequest getRequest() {
		return this.request;
	}

	public ServletResponse getResponse() {
		return this.response;
	}

	public boolean isDirectResponse() {
		return directResponse;
	}

	public void setDirectResponse(boolean directResponse) {
		this.directResponse = directResponse;
	}
}
