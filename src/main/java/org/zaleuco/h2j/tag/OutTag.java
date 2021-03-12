package org.zaleuco.h2j.tag;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.filter.cast.Converter;
import org.zaleuco.h2j.filter.cast.Shape;
import org.zaleuco.h2j.mw.XmlProcessor;

public class OutTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node attributeValue;
		String elName;
		String value;
		Node parent;
		Converter converter = null;

		String formatString = null;

		parent = node.getParentNode();

		attributes = node.getAttributes();

		attributeValue = attributes.getNamedItem("format");
		if (attributeValue != null) {
			formatString = attributeValue.getNodeValue();
		}

		attributeValue = attributes.getNamedItem("shape");
		if (attributeValue != null) {
			converter = Shape.get(attributeValue.getNodeValue());
		}

		attributeValue = attributes.getNamedItem("value");
		assertNotNull(attributeValue, "element value is missing");
		value = attributeValue.getNodeValue();

		elName = this.trasforlELname(value);
		if (elName != null) {
			if (converter != null) {
				Object object = processor.getEnviroments().getObject(elName);
				value = converter.toString(object);
			} else if (formatString != null) {
				Object object = processor.getEnviroments().getObject(elName);
				value = object != null ? String.format(formatString, object) : "";
			} else {
				value = processor.getEnviroments().getStringValue(elName);
			}
		}

		parent.replaceChild(node.getOwnerDocument().createTextNode(value), node);
	}
}
