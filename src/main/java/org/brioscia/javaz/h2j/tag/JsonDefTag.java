
package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.Enviroments;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDefTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node parent;
		Element newNode;
		Node nodeName;
		String nodeNameValue;
		Node nodeValue;
		String nodeValueValue;
		ObjectMapper mapper;
		Object object;
		String jsonString;

		attributes = node.getAttributes();

		nodeName = attributes.getNamedItem("name");
		assertNotNull(nodeName, "missing attribute name in tag jsondef");
		nodeNameValue = nodeName.getNodeValue();
		assertNotNull(nodeNameValue, "empty attribute name in tag jsondef");

		nodeValue = attributes.getNamedItem("value");
		assertNotNull(nodeValue, "missing attribute value in tag jsondef");
		nodeValueValue = nodeValue.getNodeValue();
		assertNotNull(nodeValueValue, "empty attribute value in tag jsondef");
		if (!Enviroments.isELName(nodeValueValue)) {
			throw new H2JFilterException("invalid expression: " + nodeValueValue);
		}
		nodeValueValue = nodeValueValue.substring(2, nodeValueValue.length() - 1);
		object = processor.getEnviroments().getObject(nodeValueValue);

		mapper = new ObjectMapper();
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new H2JFilterException(e);
		}

		newNode = node.getOwnerDocument().createElement("script");
		newNode.setAttribute("type", "text/javascript");
		newNode.setTextContent("var " + nodeNameValue + " = JSON.parse('" + jsonString + "');");
		parent = node.getParentNode();
		parent.insertBefore(newNode, node);
		node.getParentNode().removeChild(node);
	}
}
