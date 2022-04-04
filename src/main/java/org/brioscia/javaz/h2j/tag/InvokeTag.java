package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * &lt;invoke value="value" /&gt;
 * 
 * execute value call or call getter of the property
 * 
 * @author Zaleuco
 *
 */
public class InvokeTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		String value;
		Node parent;
		Node attributeValueNode;

		attributes = node.getAttributes();
		attributeValueNode = attributes.getNamedItem("value");

		assertNotNull(attributeValueNode, "missing attribute value in tag " + node.getNodeName() );
		assertNotNull(attributeValueNode.getNodeValue(), "missing contents for attribute value in tag " + node.getNodeName() );

		value = attributeValueNode.getNodeValue();
		if (isEL(value)) {			
			value = value.substring(2, value.length() - 1);
			processor.getEnviroments().getObject(value);
		} else {
			throw new H2JFilterException("Invalid value: " + value + " in tag reset");
		}
		
		parent = node.getParentNode();
		parent.removeChild(node);
	}

}
