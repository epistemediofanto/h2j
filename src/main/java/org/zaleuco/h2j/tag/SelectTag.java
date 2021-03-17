package org.zaleuco.h2j.tag;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.HtmlBindName;
import org.zaleuco.h2j.mw.XmlProcessor;

@Deprecated
public class SelectTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeValue;

		attributes = node.getAttributes();
		nodeValue = attributes.getNamedItem("value");

		if ((nodeValue != null) && isMapName(nodeValue.getNodeValue())) {
			String value = nodeValue.getNodeValue();
			value = value.substring(2, value.length() - 1);
			value = processor.getEnviroments().htmlName(value, null, HtmlBindName.OBJECT);
			((Element) node).setAttribute("name", value);
		}

		super.processNode(processor, node);

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
