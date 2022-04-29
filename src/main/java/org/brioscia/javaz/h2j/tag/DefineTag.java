package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * &lt;define name="varname" value="value" /&gt;
 * 
 * @author Zaleuco
 *
 */
public class DefineTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		String value;
		Node parent;
		Node attributeNameNode;
		Node attributeValueNode;

		attributes = node.getAttributes();
		attributeNameNode = attributes.getNamedItem("name");
		attributeValueNode = attributes.getNamedItem("value");

		assertNotNull(attributeNameNode, "missing attribute name in tag " + node.getNodeName());
		assertNotNull(attributeNameNode.getNodeValue(), "missing contents for attribute name in tag " + node.getNodeName());
		assertNotNull(attributeValueNode, "missing attribute value in tag " + node.getNodeName());
		assertNotNull(attributeValueNode.getNodeValue(), "missing contents for attribute value in tag " + node.getNodeName());

		value = attributeValueNode.getNodeValue();
		if (isEL(value)) {
			value = value.substring(2, value.length() - 1);
			processor.getEnviroments().pushPage(attributeNameNode.getNodeValue(),
					processor.getEnviroments().getObject(value));
		} else {
			processor.getEnviroments().pushPage(attributeNameNode.getNodeValue(), value);
		}

		parent = node.getParentNode();
		parent.removeChild(node);
	}

}
