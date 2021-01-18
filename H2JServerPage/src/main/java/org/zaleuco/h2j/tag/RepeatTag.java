package org.zaleuco.h2j.tag;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.XmlProcessor;

public class RepeatTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeValues;
		Node nodeVar;
		Node indexVar;
		Node sizeVar;
		String nameValues;
		String nameVar;
		String nameIndex;
		String nameSize;
		Node parent;

		parent = node.getParentNode();
		parent.removeChild(node);

		attributes = node.getAttributes();
		nodeValues = attributes.getNamedItem("values");
		assertNotNull(nodeValues, "element values is missing");
		nameValues = nodeValues.getNodeValue();
		assertNotEmpty(nameValues, "missing values contents");

		nodeVar = attributes.getNamedItem("var");
		assertNotNull(nodeValues, "element var is missing");
		nameVar = nodeVar.getNodeValue();
		assertNotEmpty(nameVar, "missing var contents");

		indexVar = attributes.getNamedItem("index");
		nameIndex = indexVar != null ? indexVar.getNodeValue() : null;

		sizeVar = attributes.getNamedItem("size");
		nameSize = indexVar != null ? sizeVar.getNodeValue() : null;

		nameValues = this.trasforlELname(nameValues);
		if (nameValues != null) {
			List<?> items;

			items = (List<?>) this.getEnviroments().getObject(nameValues);
			if (items != null) {
				NodeList childs;
				Node child;

				childs = node.getChildNodes();
				try {
					int j = 0;
					if (nameIndex != null) {
						this.enviroments.push(nameSize, items.size());
					}
					for (Object item : items) {
						this.enviroments.push(nameVar, item);
						if (nameIndex != null) {
							this.enviroments.push(nameIndex, ++j);
						}
						try {

							for (int i = 0; i < childs.getLength(); ++i) {
								child = childs.item(i).cloneNode(true);								
								processor.processNode(child);
								child.setUserData("h2j", "skip", null);
								parent.appendChild(child);
							}

						} finally {
							this.enviroments.pop(nameVar);
						}
						if (nameIndex != null) {
							this.enviroments.pop(nameIndex);
						}
					}
				} finally {
					this.enviroments.remove(nameVar);
					if (nameIndex != null) {
						this.enviroments.remove(nameIndex);
					}
					if (nameSize != null) {
						this.enviroments.remove(nameSize);
					}
				}
			}
		}
	}

}
