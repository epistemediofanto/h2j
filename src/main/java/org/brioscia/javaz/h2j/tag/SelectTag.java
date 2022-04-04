package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.HtmlBindName;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SelectTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeValue;

		attributes = node.getAttributes();
		nodeValue = attributes.getNamedItem("value");

		if ((nodeValue != null) && isEL(nodeValue.getNodeValue())) {
			String value = nodeValue.getNodeValue();
			value = value.substring(2, value.length() - 1);
			value = processor.resolveLoopVar(value);
			value = processor.getEnviroments().htmlName(value, null, HtmlBindName.OBJECT);
			if (((Element) node).getAttribute("name").length() == 0) {
				((Element) node).setAttribute("name", value);
			}
		}

		super.processNode(processor, node);

		nodeValue = attributes.getNamedItem("value");
		if (nodeValue != null) {
			NodeList nodeList;
			Node child, nodeMultiple;
			Node optionValue;
			String value;
			String[] values;

			value = nodeValue.getNodeValue();
			nodeMultiple = attributes.getNamedItem("multiple");
			if ((nodeMultiple != null) && ("true".equals(nodeMultiple.getNodeValue()))) {
				values = value.split(",");
			} else {
				values = new String[1];
				values[0] = value;
			}

			nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); ++i) {
				child = nodeList.item(i);
				if ("option".equals(child.getNodeName())) {
					optionValue = child.getAttributes().getNamedItem("value");
					if ((optionValue != null) && in(values, optionValue.getNodeValue())) {
						((Element) child).setAttribute("selected", "selected");
					}
				}
			}
		}
	}

	private boolean in(String[] set, String el) {
		for (String s : set) {
			if ((s != null) && (s.equals(el)))
				return true;
		}
		return false;
	}
}
