package org.zaleuco.h2j.tag;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.filter.H2JProcessorFilter;
import org.zaleuco.h2j.mw.XmlProcessor;

public class AnchorTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeAction;
		String valueAction;

		attributes = node.getAttributes();
		nodeAction = attributes.getNamedItem("href");
		assertNotNull(nodeAction, "missing href attribute on a");
		valueAction = nodeAction.getNodeValue();
		assertNotNull(valueAction, "missing href attribute on a");
		valueAction = this.enviroments.evalForHTMLCall(valueAction);
		nodeAction.setNodeValue(valueAction + H2JProcessorFilter.CALL_STRING_EXT);

		super.processNode(processor, node);
	}

}
