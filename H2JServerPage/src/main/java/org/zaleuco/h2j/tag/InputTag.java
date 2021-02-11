package org.zaleuco.h2j.tag;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.filter.cast.Converter;
import org.zaleuco.h2j.filter.cast.Shape;
import org.zaleuco.h2j.mw.XmlProcessor;

public class InputTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeValue;
		String value;

		attributes = node.getAttributes();
		nodeValue = attributes.getNamedItem("value");
		assertNotNull(nodeValue, "missing 'value' attribute in 'input' tag");

		value = nodeValue.getNodeValue();
		assertNotEmpty(value, "found empty value in attribute 'value' in 'input' tag");

		if (isMapName(value)) {
			Node converterNode;
			Converter converter = null;

			converterNode = attributes.getNamedItem("shape");
			if (converterNode != null) {
				String castName = converterNode.getNodeValue();
				assertNotEmpty(castName, "found empty value in attribute 'shape' in 'input' tag");
				converter = Shape.get(castName);
				assertNotNull(converter, "invalid converter " + castName + " in 'input' tag");
				attributes.removeNamedItem("shape");
			}

			nodeValue.setNodeValue(processor.getEnviroments().eval(value));
			value = value.substring(2, value.length() - 1);
			value = processor.getEnviroments().htmlName(value, converter);
			((Element) node).setAttribute("name", value);
		}

		super.processNode(processor, node);
	}

}
