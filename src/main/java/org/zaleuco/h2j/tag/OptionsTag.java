package org.zaleuco.h2j.tag;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.XmlProcessor;
import org.zaleuco.h2j.tagstruct.Options;

@Deprecated
public class OptionsTag extends BaseTag {

	@SuppressWarnings("unchecked")
	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node attributeValue;
		String name;
		Node parent;

		parent = node.getParentNode();
		parent.removeChild(node);

		attributes = node.getAttributes();
		attributeValue = attributes.getNamedItem("values");
		if (attributeValue != null) {
			name = attributeValue.getNodeValue();
			name = this.trasforlELname(name);
			if (name != null) {
				List<Options> items;

				items = (List<Options>) processor.getEnviroments().getObject(name);
				if (items != null) {
					Element element;
					String uri;
					for (Options o : items) {
						uri = node.getBaseURI();
						element = node.getOwnerDocument().createElementNS(uri, "option");
						element.setAttributeNS(uri, "value", o.getValue());
						this.writeAttributes(processor.getEnviroments(), element, attributes, "values");
						element.appendChild(node.getOwnerDocument().createTextNode(o.getLabel()));
						parent.appendChild(element);
					}
				}
			}
		}
	}

}
