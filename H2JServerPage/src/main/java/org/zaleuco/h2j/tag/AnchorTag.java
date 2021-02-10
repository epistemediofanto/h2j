package org.zaleuco.h2j.tag;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.filter.H2JProcessorFilter;
import org.zaleuco.h2j.filter.cast.Converter;
import org.zaleuco.h2j.mw.XmlProcessor;

public class AnchorTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeAction;
		String value;
		Converter converter = null;

		attributes = node.getAttributes();
		nodeAction = attributes.getNamedItem("href");
		assertNotNull(nodeAction, "missing href attribute in a tag");

		value = nodeAction.getNodeValue();
		assertNotEmpty(value, "found empty value in attribute href in a tag");

		if (isMapName(value)) {
			value = value.substring(2, value.length() - 1);
			value = processor.getEnviroments().evalForHTMLCall(value);
			value = processor.getEnviroments().htmlName(value, converter);
			nodeAction.setNodeValue(value + H2JProcessorFilter.CALL_STRING_EXT);
		}

		super.processNode(processor, node);
	}

}
