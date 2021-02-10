package org.zaleuco.h2j.tag;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.filter.H2JProcessorFilter;
import org.zaleuco.h2j.filter.cast.Converter;
import org.zaleuco.h2j.mw.XmlProcessor;

public class ButtonTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeAction;
		String valueAction;
		Converter converter=null;

		attributes = node.getAttributes();
		nodeAction = attributes.getNamedItem("formaction");
		assertNotNull(nodeAction, "missing 'formaction' attribute in 'button' tag");

		valueAction = nodeAction.getNodeValue();
		assertNotEmpty(valueAction, "found empty value in attribute 'formaction' in 'button' tag");

		if (isMapName(valueAction)) {
			valueAction = valueAction.substring(2, valueAction.length() - 1);
			valueAction = processor.getEnviroments().evalForHTMLCall(valueAction);
			valueAction = processor.getEnviroments().htmlName(valueAction, converter);
			nodeAction.setNodeValue(valueAction + H2JProcessorFilter.CALL_STRING_EXT);
		}
		
		super.processNode(processor, node);
	}

}
