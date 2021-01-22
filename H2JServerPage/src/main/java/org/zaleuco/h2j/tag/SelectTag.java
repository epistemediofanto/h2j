package org.zaleuco.h2j.tag;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.XmlProcessor;

public class SelectTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeValue;

		super.processNode(processor, node);

		attributes = node.getAttributes();
		nodeValue = attributes.getNamedItem("value");
		if (nodeValue != null) {
			NodeList nodeList;
			Node child;
			Node optionValue;
			String value;

			value = nodeValue.getNodeValue();
			nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); ++i) {
				child = nodeList.item(i);
				if ("option".equals(child.getNodeName())) {
					optionValue = child.getAttributes().getNamedItem("value");
					if ((optionValue != null) && value.equals(optionValue.getNodeValue())) {
						((Element) child).setAttribute("selected", "selected");
					}
				}
			}
		}
	}
}
