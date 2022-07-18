package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DefaultHtmlTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		this.processAttributes(processor, node.getAttributes());
		this.processNodes(processor, node);
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
				value = this.valueRoot(processor, value);
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
