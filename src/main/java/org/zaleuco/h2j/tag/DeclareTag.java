package org.zaleuco.h2j.tag;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.XmlProcessor;

public class DeclareTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeVar;
		Node nodeBean;
		String nameVar;
		String valueBean;
		Node parent;
		Object object;

		parent = node.getParentNode();
		parent.removeChild(node);

		attributes = node.getAttributes();

		nodeVar = attributes.getNamedItem("var");
		assertNotNull(nodeVar, "missing 'var' attribute in 'addList' tag");
		nameVar = nodeVar.getNodeValue();
		assertNotEmpty(nameVar, "found empty value in attribute 'var' in 'addList' tag");

		nodeBean = attributes.getNamedItem("bean");
		assertNotNull(nodeBean, "missing 'bean' attribute in 'addList' tag");
		valueBean = nodeBean.getNodeValue();
		assertNotEmpty(valueBean, "found empty value in attribute 'bean' in 'addList' tag");

		try {
			if (isMapName(valueBean)) {
				object = processor.getEnviroments().getObject(valueBean.substring(2, valueBean.length() - 1));
			} else {

				Class<?> clz;
				Constructor<?> constructor;

				clz = Class.forName(valueBean);
				constructor = clz.getDeclaredConstructor();
				object = constructor.newInstance();
			}
			processor.getEnviroments().push(nameVar, object);
		} catch (NoSuchMethodException e) {
			throw new H2JFilterException(valueBean + " missing default constructor", e);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new H2JFilterException(node.toString(), e);
		}

	}

}
