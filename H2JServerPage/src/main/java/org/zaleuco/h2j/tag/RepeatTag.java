package org.zaleuco.h2j.tag;

import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.Enviroments;
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
		Enviroments enviroments;

		enviroments = processor.getEnviroments();

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
			Object oList;

			oList = enviroments.getObject(nameValues);
			if (!(oList instanceof List<?>)) {
				String type = oList != null ? "Found " + oList + " (" + oList.getClass().getName() + "), " : "";
				throw new H2JFilterException(type + "expected List<?> value from " + nameValues);
			}
			items = (List<?>) oList;
			if (items != null) {
				NodeList childs;
				Node child;

				childs = node.getChildNodes();
				try {
					int j = 0;
					if (nameIndex != null) {
						enviroments.push(nameSize, items.size());
					}
					for (Object item : items) {
						enviroments.push(nameVar, item);
						if (nameIndex != null) {
							enviroments.push(nameIndex, ++j);
						}
						try {

							// Ciclo REPEAT
							for (int i = 0; i < childs.getLength(); ++i) {
								child = childs.item(i).cloneNode(true);
								parent.appendChild(child);
								processor.processNode(child);
								child.setUserData("h2j", "skip", null);								
							}

						} finally {
							enviroments.pop(nameVar);
						}
						if (nameIndex != null) {
							enviroments.pop(nameIndex);
						}
					}
				} finally {
					enviroments.remove(nameVar);
					if (nameIndex != null) {
						enviroments.remove(nameIndex);
					}
					if (nameSize != null) {
						enviroments.remove(nameSize);
					}
				}
			}
		}
	}

}
