package org.zaleuco.h2j.tag;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.filter.H2JProcessorFilter;
import org.zaleuco.h2j.filter.cast.Converter;
import org.zaleuco.h2j.mw.HtmlBindName;
import org.zaleuco.h2j.mw.XmlProcessor;

public class FormTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeAction;
		String value;
		Converter converter = null;

		attributes = node.getAttributes();
		nodeAction = attributes.getNamedItem("action");
		assertNotNull(nodeAction, "missing 'action' attribute in 'form' tag");

		value = nodeAction.getNodeValue();
		assertNotNull(value, "found empty value in attribute 'action' in 'from' tag");

		if (isMapName(value)) {
			value = value.substring(2, value.length() - 1);
			value = processor.getEnviroments().evalForHTMLCall(value);
			value = processor.getEnviroments().htmlName(value, converter, HtmlBindName.DYNAMIC_CALL);
			nodeAction.setNodeValue(value + H2JProcessorFilter.CALL_STRING_EXT);
		}

		super.processNode(processor, node);
	}

}
