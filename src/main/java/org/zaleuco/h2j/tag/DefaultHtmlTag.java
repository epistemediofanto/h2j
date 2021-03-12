package org.zaleuco.h2j.tag;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.XmlProcessor;

public class DefaultHtmlTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {

//		if (in(node.getNodeName(), "input")) {
//			hideNodeAttribute(processor, node, "name", "value");
//		} else if (in(node.getNodeName(), "a")) {
//			if (hideNodeAttribute(processor, node, "href", "href")) {
//				Node href = node.getAttributes().getNamedItem("href");
//				href.setNodeValue(href.getNodeValue() + H2JProcessorFilter.CALL_STRING_EXT);
//			}
//		}

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

		nodeName = node.getNodeName();
		if ("value".equals(nodeName)) {
			node.setNodeValue(processor.getEnviroments().eval(node.getNodeValue()));
		}
	}

}
