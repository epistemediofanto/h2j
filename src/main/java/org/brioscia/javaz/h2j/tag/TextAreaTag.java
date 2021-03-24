package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.filter.cast.Converter;
import org.brioscia.javaz.h2j.filter.cast.Shape;
import org.brioscia.javaz.h2j.mw.HtmlBindName;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TextAreaTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeValue;
		String value;

		attributes = node.getAttributes();
		nodeValue = attributes.getNamedItem("value");
		assertNotNull(nodeValue, "missing 'value' attribute in 'textarea' tag");

		value = nodeValue.getNodeValue();
		assertNotEmpty(value, "found empty text in attribute 'value' in 'textarea' tag");

		if (isMapName(value)) {
			Converter converter = null;
			Node converterNode;
			String name = value;

			converterNode = attributes.getNamedItem("shape");
			if (converterNode != null) {
				String castName = converterNode.getNodeValue();
				assertNotEmpty(castName, "found empty value in attribute 'shape' in 'input' tag");
				converter = Shape.get(castName);
				assertNotNull(converter, "invalid converter " + castName + " in 'input' tag");
				attributes.removeNamedItem("shape");
			}

			if (converter != null) {
				value = converter
						.toString(processor.getEnviroments().getObject(value.substring(2, value.length() - 1)));
			} else {
				value = processor.getEnviroments().eval(value);
			}
			
			node.appendChild(node.getOwnerDocument().createTextNode(value));

			name = name.substring(2, name.length() - 1);
			name = processor.getEnviroments().htmlName(name, converter, HtmlBindName.DYNAMIC_CALL);
			((Element) node).setAttribute("name", name);
		}

		super.processNode(processor, node);
	}

}
