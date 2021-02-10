package org.zaleuco.h2j.mw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

import org.zaleuco.expression.EnvContext;
import org.zaleuco.expression.InvokerException;

public class Store extends HtmlBindName implements EnvContext {

	private static final long serialVersionUID = 1L;
	private HashMap<String, List<Object>> storeSpace;
	private boolean searchInCDI = true;

	protected Store() {
		this.storeSpace = new HashMap<String, List<Object>>();
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
		list = this.storeSpace.get(element);
		if (list == null) {
			list = new ArrayList<Object>();
			this.storeSpace.put(element, list);
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
		list = this.storeSpace.get(element);
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
		list = this.storeSpace.get(element);
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
		list = this.storeSpace.get(element);
		if ((list != null) && (list.size() == 0)) {
			this.storeSpace.remove(element);
		}
	}

	public static Object getCDIObject(String objectName) throws InvokerException {
		Object object = null;

		BeanManager bm;
		CreationalContext<?> ctx;
		Bean<?> bean;
		Set<Bean<?>> set;
		Iterator<Bean<?>> iter;

		bm = CDI.current().getBeanManager();
		set = bm.getBeans(objectName);
		if (set == null) {
			throw new InvokerException("cannot invoke " + objectName + " to object null");
		}
		iter = set.iterator();
		if (iter.hasNext()) {

			bean = iter.next();
			ctx = bm.createCreationalContext(bean);

			object = bm.getReference(bean, bean.getBeanClass(), ctx);
		}
		return object;
	}

	public Object get(String name) throws InvokerException {
		Object object;
		object = this.peek(name);
		if ((object == null) && this.searchInCDI) {
			object = getCDIObject(name);
		}
		return object;
	}

	public void enableCDIContext() {
		this.searchInCDI = true;
	}

	public void disableCDIContext() {
		this.searchInCDI = false;
	}
}
