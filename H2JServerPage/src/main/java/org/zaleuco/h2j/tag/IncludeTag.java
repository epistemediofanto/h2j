package org.zaleuco.h2j.tag;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.XmlProcessor;

public class IncludeTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node attributeValue;
		Node parent;

		parent = node.getParentNode();

		attributes = node.getAttributes();
		attributeValue = attributes.getNamedItem("file");
		if (attributeValue != null) {
			String file;
			InputStream is = null;
			Document doc;
			Node includeNode;

			file = processor.getPath() + this.evaluation(attributeValue.getNodeValue());

			try {
				is = this.enviroments.getServletContext().getResourceAsStream(file);
				doc = processor.load(is);
				is.close();
				is = null;
				includeNode = node.getOwnerDocument().importNode(doc.getDocumentElement(), true);
				parent.replaceChild(includeNode, node);
				processor.processNode(includeNode);
				node = null;
			} catch (IOException | ParserConfigurationException | SAXException e) {
				throw new H2JFilterException(e);
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

		if (node != null) {
			parent.removeChild(node);
		}
	}

}
