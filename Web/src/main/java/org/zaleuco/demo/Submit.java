package org.zaleuco.demo;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class Submit {
	
	@Inject
	private DemoBean demoBean;
	
	public String navigate(String page, DemoBean demoBean) {
		return page;
	}
}
