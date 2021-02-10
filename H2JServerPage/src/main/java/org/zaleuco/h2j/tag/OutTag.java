package org.zaleuco.h2j.tag;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.XmlProcessor;

public class OutTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node attributeValue;
		String elName;
		String value;
		Node parent;
		
		String formatString = null;

		parent = node.getParentNode();

		attributes = node.getAttributes();

		attributeValue = attributes.getNamedItem("format");
		if (attributeValue != null) {
			formatString = attributeValue.getNodeValue();
		}

		attributeValue = attributes.getNamedItem("value");
		assertNotNull(attributeValue, "element value is missing");
		value = attributeValue.getNodeValue();

		elName = this.trasforlELname(value);
		if (elName != null) {
			if (formatString != null) {
				Object object = processor.getEnviroments().getObject(elName);
				value = String.format(formatString, object);
			} else {
				value = processor.getEnviroments().getStringValue(elName);
			}
		}

		parent.replaceChild(node.getOwnerDocument().createTextNode(value), node);
	}
}
