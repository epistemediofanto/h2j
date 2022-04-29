package org.brioscia.javaz.h2j.tag;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.Enviroments;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class IncludeTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node attributeFileNode;
		Node attributeNameNode;
		Node attributeValueNode;
		Node parent;

		parent = node.getParentNode();

		attributes = node.getAttributes();
		attributeFileNode = attributes.getNamedItem("file");
		assertNotNull(attributeFileNode, "missing attribute file");

		attributeNameNode = attributes.getNamedItem("name");
		attributeValueNode = attributes.getNamedItem("value");

		if (attributeNameNode != null) {
			assertNotNull(attributeNameNode.getNodeValue(), "missing contents for attribute name");
			assertNotNull(attributeValueNode, "missing attribute value");
			assertNotNull(attributeValueNode.getNodeValue(), "missing contents for attribute value");
		}

		String file;
		Document doc;
		Node includeNode;
		String path;
		String currentPath;
		
		currentPath = processor.getPath();
		
		file = this.valueRoot(processor, attributeFileNode.getNodeValue());
		file =  this.evaluation(processor.getEnviroments(), file);
				
		if (file.startsWith("/")) {
			path = XmlProcessor.extractPath(file);
		} else {
			path = currentPath + XmlProcessor.extractPath(file);
			file =  path + XmlProcessor.extractFileName(file);
		}	
		processor.setPath(path);
		
		try {
			this.processNodes(processor, node.getChildNodes());
			doc = Enviroments.getFileSystem().loadDocument(file);

			includeNode = node.getOwnerDocument().importNode(doc.getDocumentElement(), true);
			parent.replaceChild(includeNode, node);

			try {
				if (attributeNameNode != null) {
					String value = attributeValueNode.getNodeValue();
					if (isEL(value)) {
						value = value.substring(2, value.length() - 1);
						processor.getEnviroments().push(attributeNameNode.getNodeValue(),
								processor.getEnviroments().getObject(value));
					} else {
						processor.getEnviroments().push(attributeNameNode.getNodeName(), value);
					}

				}
				processor.processNode(includeNode);
			} finally {
				if (attributeNameNode != null) {
					processor.getEnviroments().pop(attributeNameNode.getNodeValue());
				}
			}
			node = null;

		} catch (IOException | ParserConfigurationException | SAXException e) {
			throw new H2JFilterException(file, e);
		} finally {
			processor.setPath(currentPath);
		}
	}

}
