package org.zaleuco.h2j.tag;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.filter.H2JProcessorFilter;
import org.zaleuco.h2j.mw.XmlProcessor;

public class FormTag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeAction;
		Node nodeName;
		String valueAction;
		String valueName;

		attributes = node.getAttributes();
		nodeAction = attributes.getNamedItem("action");
		assertNotNull(nodeAction, "missing action attribute on form");
		valueAction = nodeAction.getNodeValue();
		assertNotNull(nodeAction, "missing action attribute on form");

		valueAction = this.trasforlELname(valueAction);
		if (valueAction != null) {
			attributes = node.getAttributes();
			nodeName = attributes.getNamedItem("name");
			assertNotNull(nodeName, "missing name attribute on form");
			valueName = nodeName.getNodeValue();
			assertNotNull(valueName, "missing name attribute on form");

			nodeAction.setNodeValue(valueAction + H2JProcessorFilter.CALL_STRING_EXT);			
		}

		super.processNode(processor, node);
	}

}
