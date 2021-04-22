package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.filter.H2JProcessorFilter;
import org.brioscia.javaz.h2j.filter.cast.Converter;
import org.brioscia.javaz.h2j.mw.HtmlBindName;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class FormTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeAction;
		String valueAction;
//		Node nodeJson;
//		String valueJson;
		Converter converter = null;

		attributes = node.getAttributes();

		nodeAction = attributes.getNamedItem("action");
		assertNotNull(nodeAction, "missing 'action' attribute in 'form' tag");
		valueAction = nodeAction.getNodeValue();
		assertNotNull(valueAction, "found empty value in attribute 'action' in 'from' tag");
		
//		nodeJson = attributes.getNamedItem("json");
//		valueJson = nodeJson != null ? nodeJson.getNodeValue() : null;
		
		if (isEL(valueAction)) {
			valueAction = valueAction.substring(2, valueAction.length() - 1);
			valueAction = processor.getEnviroments().evalForHTMLCall(valueAction);
			valueAction = processor.getEnviroments().htmlName(valueAction, converter, HtmlBindName.DYNAMIC_CALL);
			nodeAction.setNodeValue(valueAction + H2JProcessorFilter.CALL_STRING_EXT);
		}

		super.processNode(processor, node);
		
	}

}
