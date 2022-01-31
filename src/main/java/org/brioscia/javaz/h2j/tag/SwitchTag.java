package org.brioscia.javaz.h2j.tag;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.Enviroments;
import org.brioscia.javaz.h2j.mw.LoopVar;
import org.brioscia.javaz.h2j.mw.Store.SetMode;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * &lt;switch value='expression' &gt;
 * 
 * 		&lt;case value='value'&gt; ... &lt;/case&gt;
 * 		&lt;case value='value'&gt; ... &lt;/case&gt;
 * 
 * &lt;/switch&gt;
 * 
 * @author Zaleuco
 *
 */
public class SwitchTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeValue;
		String value;
		Enviroments enviroments;Node parent;
		
		attributes = node.getAttributes();
		nodeValue = attributes.getNamedItem("value");
		assertNotNull(nodeValue, "missing 'value' attribute in 'switch' tag");
		
		value = nodeValue.getNodeValue();
		assertNotEmpty(value, "empty 'value' attribute in 'switch' tag");
		
		enviroments = processor.getEnviroments();
		value = enviroments.eval(value);
		
		parent = node.getParentNode();
		this.processCase(processor, parent, node, value);
				
		parent.removeChild(node);
	}

	private void processCase(XmlProcessor processor, Node parent, Node node, String value) throws DOMException, H2JFilterException {
		NodeList childs;
		Node child;
		String name;
		String caseValue;
		Enviroments enviroments;
		
		enviroments = processor.getEnviroments();
		childs = node.getChildNodes();
		for (int i=0; i<childs.getLength(); ++i) {
			child = childs.item(i);			
			name = child.getLocalName();
			if ("case".equals(name)) {
				NamedNodeMap attributes;
				Node caseNodeValue;
				
				attributes = child.getAttributes();
				caseNodeValue = attributes.getNamedItem("value");
				assertNotNull(caseNodeValue, "missing value in case");
				caseValue = caseNodeValue.getNodeValue();
				assertNotNull(caseValue, "empty value in case");
				caseValue = enviroments.eval(caseValue);
				if (value.equals(caseValue)) {
					this.processNode(processor, parent, child);
					break;
				}				
			} else if ("default".equals(name)) {
				this.processNode(processor, parent, child);
				break;
			}
		}
	}

	private void processNode(XmlProcessor processor, Node parent, Node node) throws H2JFilterException {
		NodeList childs;
		
		childs = node.getChildNodes();
		for (int i=0; i<childs.getLength(); ++i) {
			parent.appendChild(childs.item(i));
		}
	}
}
