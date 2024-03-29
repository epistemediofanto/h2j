package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.filter.cast.Converter;
import org.brioscia.javaz.h2j.filter.cast.Shape;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class OutTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node attributeValue;
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
		
		if (isEL(value)) {
			String elName;
			elName = value.substring(2, value.length() - 1);
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
