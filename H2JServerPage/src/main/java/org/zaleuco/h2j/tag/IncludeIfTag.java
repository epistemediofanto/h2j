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

public class IncludeIfTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node expNode;
		Node thenNode;
		Node parent;
		String expValue;
		String thenValue;
//		String elName;
		String value;
		String file = null;

		parent = node.getParentNode();

		attributes = node.getAttributes();
		expNode = attributes.getNamedItem("exp");
		assertNotNull(expNode, "element exp is missing");
		expValue = expNode.getNodeValue();
		assertNotEmpty(expValue, "missing expression contents");

		thenNode = attributes.getNamedItem("then");
		assertNotNull(expNode, "element then is missing");
		thenValue = thenNode.getNodeValue();

//		elName = this.trasforlELname(expValue);
//		if (elName != null) {
//			value = processor.getEnviroments().getStringValue(elName);
//		} else {
//			value = expValue;
//		}
		value = processor.getEnviroments().eval(expValue);
		
		if ("true".equals(value)) {
			file = thenValue;
		} else {
			Node elseNode;

			elseNode = attributes.getNamedItem("else");
			if (elseNode != null) {
				file = elseNode.getNodeValue();
			}
		}

		if ((file != null) && (file.length() > 0)) {
			InputStream is = null;
			Document doc;
			Node includeNode;

			file = processor.getPath() + file;

			try {
//				is = Enviroments.getServletContext().getResourceAsStream(file);
				is = Enviroments.getFileSystem().load(file);
				assertNotNull(is, "file not found: " + file);
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
