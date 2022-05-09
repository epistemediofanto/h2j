package org.brioscia.javaz.h2j.filter;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.brioscia.javaz.h2j.mw.Enviroments;

@Named
@SessionScoped
public class H2JContext implements Serializable {

	private static final long serialVersionUID = 1L;
	private ServletRequest request;
	private ServletResponse response;
	private boolean directResponse = false;
	private H2JProcessorFilter filter;
	private Enviroments enviroments;
	private boolean refresh = false;

	void set(H2JProcessorFilter filter, Enviroments enviroments, ServletRequest request, ServletResponse response) {
		this.request = request;
		this.response = response;
		this.directResponse = false;
		this.filter = filter;
		this.enviroments = enviroments;
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

	public boolean processReuest() throws H2JFilterException {
		return this.filter.processRequest(this.enviroments, (HttpServletRequest) this.request,
				(HttpServletResponse) this.response);
	}

	public boolean processRequest(ServletRequest request, ServletResponse response) throws H2JFilterException {
		return this.filter.processRequest(this.enviroments, (HttpServletRequest) request,
				(HttpServletResponse) response);
	}

	public void processResponse(String page) throws H2JFilterException {
		this.filter.processResponse(this.enviroments, page, (HttpServletRequest) this.request,
				(HttpServletResponse) this.response);
	}

	public void processResponse(ServletRequest request, ServletResponse response, String page)
			throws H2JFilterException {
		this.filter.processResponse(this.enviroments, page, (HttpServletRequest) request,
				(HttpServletResponse) response);
	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}
	
	public Enviroments getEnviroments() {
		return this.enviroments;
	}

	// /**
//	 * Forwards a request from a current resource to another resource 
//	 * 
//	 * @param page a String specifying the pathname to the resource. If it is relative, it must be relative against the current servlet.
//	 * @throws ServletException if the target resource throws this exception
//	 * @throws IOException if the target resource throws this exception
//	 * @throws IllegalStateException - if the response was already committed
//	 */
//	public void forward(String page) throws ServletException, IOException {
//	      RequestDispatcher requestDispatcher = this.request.getRequestDispatcher(page);	      
//	      requestDispatcher.forward(this.request, this.response);
//	 
//	}
}
