package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.filter.H2JProcessorFilter;
import org.brioscia.javaz.h2j.filter.cast.Converter;
import org.brioscia.javaz.h2j.mw.HtmlBindName;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ATag extends DefaultH2JTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeAction;
		String value;
		String remoteValue;
		boolean remote;
		Converter converter = null;

		attributes = node.getAttributes();

		nodeAction = attributes.getNamedItem("remote");
		remoteValue = nodeAction != null ? nodeAction.getNodeValue() : "true";
		remote = (remoteValue == null) || (!"false".equals(remoteValue));

		nodeAction = attributes.getNamedItem("href");
		assertNotNull(nodeAction, "missing href attribute in a tag");

		value = nodeAction.getNodeValue();
		assertNotEmpty(value, "found empty value in attribute href in a tag");
		
		value = this.valueRoot(processor, value);
		if (isEL(value)) {
			value = processor.getEnviroments().evalForHTMLCall(value);
			if (remote) {
				value = processor.getEnviroments().htmlName(value, converter, HtmlBindName.DYNAMIC_CALL);
				nodeAction.setNodeValue(value + H2JProcessorFilter.CALL_STRING_EXT);
			} else {
				nodeAction.setNodeValue(value);
			}
		}

		super.processNode(processor, node);
	}

}
