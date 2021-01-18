package org.zaleuco.h2j.mw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalEnviroment extends Assertion {
	
	private HashMap<String, List<Object>> localEnviroments;

	protected LocalEnviroment() {
		this.localEnviroments = new HashMap<String, List<Object>>();
	}
	
	/**
	 * 
	 * insert element into the stack
	 * 
	 * @param element
	 * @param value
	 */
	public void push(String element, Object value) {
		List<Object> list;
		list = this.localEnviroments.get(element);
		if (list == null) {
			list = new ArrayList<Object>();
			this.localEnviroments.put(element, list);
		}
		list.add(0, value);
	}

	/**
	 * 
	 * read first stack element, but don't remove it
	 * 
	 * @param element
	 * @return
	 */
	public Object peek(String element) {
		Object value = null;
		List<Object> list;
		list = this.localEnviroments.get(element);
		if (list != null) {
			value = list.get(0);
		}
		return value;
	}

	/**
	 * 
	 * remove the element from the stack
	 * 
	 * @param element
	 * @return
	 */
	public Object pop(String element) {
		Object value = null;
		List<Object> list;
		list = this.localEnviroments.get(element);
		if (list != null) {
			value = list.remove(0);
		}
		return value;
	}

	/**
	 * disallocates the space for the element
	 * 
	 * @param element
	 */
	public void remove(String element) {
		List<Object> list;
		list = this.localEnviroments.get(element);
		if ((list != null) && (list.size() == 0)) {
			this.localEnviroments.remove(element);
		}
	}

	
}
