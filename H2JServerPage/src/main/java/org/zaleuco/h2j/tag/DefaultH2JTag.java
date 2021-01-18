package org.zaleuco.h2j.tag;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.XmlProcessor;

public class DefaultH2JTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		node.setPrefix("");
		this.processAttributes(processor, node.getAttributes());
		this.processNodes(processor, node.getChildNodes());
	}

	protected void processNodes(XmlProcessor processor, NodeList nodeList) throws H2JFilterException {
		if (nodeList != null) {
			Node n;
			short s;
			for (int i = 0; i < nodeList.getLength(); ++i) {
				n = nodeList.item(i);
				s = n.getNodeType();
				if (s == Node.ELEMENT_NODE) {
					processor.processNode(nodeList.item(i));
				}
			}
		}
	}

	protected void processAttributes(XmlProcessor processor, NamedNodeMap attributes) throws H2JFilterException {
		if (attributes != null) {
			Node n;
			String prefix;
			String value;
			short s;
			for (int i = 0; i < attributes.getLength(); ++i) {
				n = attributes.item(i);
				s = n.getNodeType();
				prefix = n.getPrefix();
				value = n.getNodeValue();
System.out.println("CORREGGERE: " + prefix);
				if (s == Node.ATTRIBUTE_NODE) {
					if ("xmlns".equals(prefix) && XmlProcessor.NAMESPACE.equals(value)) {
						attributes.removeNamedItem(n.getNodeName());
					} else {
						this.processAttribute(processor, attributes.item(i));
					}
				}
			}
		}
	}

	protected void processAttribute(XmlProcessor processor, Node node) throws H2JFilterException {
		String nodeName;
		String nodeValue;

		nodeValue = node.getNodeValue();
		nodeName = this.trasforlELname(nodeValue);
		if (nodeName != null) {
			nodeValue = this.getEnviroments().getValue(nodeName);
			node.setNodeValue(nodeValue);
		}
	}

}
