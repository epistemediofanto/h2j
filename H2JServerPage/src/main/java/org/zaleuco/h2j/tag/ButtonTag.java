package org.zaleuco.h2j.tag;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.filter.H2JProcessorFilter;
import org.zaleuco.h2j.mw.XmlProcessor;

public class ButtonTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeAction;
		String valueAction;

		attributes = node.getAttributes();
		nodeAction = attributes.getNamedItem("formaction");
		assertNotNull(nodeAction, "missing formaction attribute on button");
		valueAction = nodeAction.getNodeValue();
		assertNotNull(valueAction, "missing formaction attribute on button");
		valueAction = processor.getEnviroments().evalForHTMLCall(valueAction);
		nodeAction.setNodeValue(valueAction + H2JProcessorFilter.CALL_STRING_EXT);

		super.processNode(processor, node);
	}

}
