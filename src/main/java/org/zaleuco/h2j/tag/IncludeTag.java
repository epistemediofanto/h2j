package org.zaleuco.h2j.tag;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.Enviroments;
import org.zaleuco.h2j.mw.XmlProcessor;

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
		InputStream is = null;
		Document doc;
		Node includeNode;

		file = processor.getPath() + this.evaluation(processor.getEnviroments(), attributeFileNode.getNodeValue());

		try {
			is = Enviroments.getFileSystem().load(file);
			doc = processor.load(is);
			is.close();
			is = null;
			includeNode = node.getOwnerDocument().importNode(doc.getDocumentElement(), true);
			parent.replaceChild(includeNode, node);
			try {
				if (attributeNameNode != null) {
					String value = attributeValueNode.getNodeValue();
					if (isMapName(value)) {
						value = value.substring(2, value.length() - 1);
						processor.getEnviroments().push(attributeNameNode.getNodeValue(), processor.getEnviroments().getObject(value));
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
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
