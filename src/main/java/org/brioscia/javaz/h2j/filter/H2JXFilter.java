package org.brioscia.javaz.h2j.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public class H2JXFilter extends H2JProcessorFilter {

	private static final long serialVersionUID = 1L;

	public void init(FilterConfig fConfig) throws ServletException {
		super.init(fConfig);
		System.out.println("Filter started at: " + fConfig.getServletContext().getContextPath());
	}
}
