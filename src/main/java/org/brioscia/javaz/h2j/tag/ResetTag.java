package org.brioscia.javaz.h2j.tag;

import java.util.List;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * &lt;reset value="propertylist" /&gt;
 * 
 * propertylist must be a list, the tag clear and initialize the property list
 * 
 * @author Zaleuco
 *
 */
public class ResetTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		String value;
		Node parent;
		Node attributeValueNode;

		attributes = node.getAttributes();
		attributeValueNode = attributes.getNamedItem("value");

		assertNotNull(attributeValueNode, "missing attribute value");
		assertNotNull(attributeValueNode.getNodeValue(), "missing contents for attribute value");

		value = attributeValueNode.getNodeValue();
		if (isEL(value)) {
			Object object;
			value = value.substring(2, value.length() - 1);
			object = processor.getEnviroments().getObject(value);
			if (object instanceof List) {
				((List<?>) object).clear();
			} else {
				throw new H2JFilterException("Invalid type of " + value + " expected instance of java.lang.List in tag reset");
			}
		} else {
			throw new H2JFilterException("Invalid value: " + value + " in tag reset");
		}
		
		parent = node.getParentNode();
		parent.removeChild(node);
	}

}
