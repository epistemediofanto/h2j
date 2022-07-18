package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.Enviroments;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * &lt;if value='expression' &gt;
 * 
 * &lt;then'&gt; ... &lt;/then&gt;
 * 
 * [ &lt;else&gt;... &lt;/else&gt; ] &lt;
 * 
 * /if%gt;
 * 
 * @author Zaleuco
 *
 */
public class IfTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeValue;
		String value;
		Enviroments enviroments;
		Node parent;

		attributes = node.getAttributes();
		nodeValue = attributes.getNamedItem("value");
		assertNotNull(nodeValue, "missing 'value' attribute in 'if' tag");

		value = nodeValue.getNodeValue();
		assertNotEmpty(value, "empty 'value' attribute in 'switch' tag");

		enviroments = processor.getEnviroments();
		value = enviroments.eval(value);

		parent = node.getParentNode();
		this.processCase(processor, parent, node, value);
	
	}

	private void processCase(XmlProcessor processor, Node parent, Node node, String value) throws DOMException, H2JFilterException {
		NodeList childs;
		Node child;
		String name;

		childs = node.getChildNodes();
		for (int i = 0; i < childs.getLength(); ++i) {
			child = childs.item(i);
			name = child.getLocalName();
			if ("then".equals(name) && "true".equals(value)) {
				this.processNodes(processor, parent, node, child);
			}
			if ("else".equals(name) && "false".equals(value)) {
				this.processNodes(processor, parent, node, child);
			}
		}
	}

}
