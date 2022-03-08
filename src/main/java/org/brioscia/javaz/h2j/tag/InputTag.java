package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.filter.cast.Converter;
import org.brioscia.javaz.h2j.filter.cast.Shape;
import org.brioscia.javaz.h2j.mw.HtmlBindName;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class InputTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeValue;
		String value, originalValue;

		attributes = node.getAttributes();
		nodeValue = attributes.getNamedItem("value");
		assertNotNull(nodeValue, "missing 'value' attribute in 'input' tag");

		value = nodeValue.getNodeValue();
		assertNotEmpty(value, "found empty value in attribute 'value' in 'input' tag");

		if (isEL(value)) {
			Node converterNode;
			Converter converter = null;
			String valueName;

			converterNode = attributes.getNamedItem("shape");
			if (converterNode != null) {
				String castName = converterNode.getNodeValue();
				assertNotEmpty(castName, "found empty value in attribute 'shape' in 'input' tag");
				converter = Shape.get(castName);
				assertNotNull(converter, "invalid converter " + castName + " in 'input' tag");
				attributes.removeNamedItem("shape");
			}

			originalValue = value.substring(2, value.length() - 1);
			valueName = processor.resolveLoopVar(originalValue);
			// patch del 22/02/2022 errore input in td 
			valueName = originalValue;
			if (converter != null) {
				Object obj = processor.getEnviroments().getObject(valueName);
				try {			
					nodeValue.setNodeValue(converter.toString(obj));
				} catch (ClassCastException e) {
					throw new H2JFilterException("Cannot cast " + obj + " to " + converter.getName() + " in " + nodeValue, e);
				}
			} else {
				nodeValue.setNodeValue(processor.getEnviroments().eval(value));
			}

			value = processor.getEnviroments().htmlName(valueName, converter, HtmlBindName.DYNAMIC_CALL);

//			if (processor.getEnviroments().getMode() == SetMode.list) {
//				((Element) node).setAttribute("name", value + "$");
//			} else {
			((Element) node).setAttribute("name", value);
//			}

		}

		super.processNode(processor, node);
	}

}
