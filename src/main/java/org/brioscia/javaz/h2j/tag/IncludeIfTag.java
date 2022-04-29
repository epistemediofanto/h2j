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

public class IncludeIfTag extends BaseTag {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node expNode;
		Node thenNode;
		Node parent;
		String expValue;
		String thenValue;

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
			Document doc;
			Node includeNode;
			String path;
			String currentPath = processor.getPath();

			file = this.valueRoot(processor, file);
			file = this.evaluation(processor.getEnviroments(), file);
			
			path = currentPath + XmlProcessor.extractPath(file);
			if (!file.startsWith("/")) {
				file =  path + XmlProcessor.extractFileName(file);
			}	
			processor.setPath(path);				
			
			try {
				doc = Enviroments.getFileSystem().loadDocument(file);
				includeNode = node.getOwnerDocument().importNode(doc.getDocumentElement(), true);
				parent.replaceChild(includeNode, node);
				processor.processNode(includeNode);
				node = null;
			} catch (IOException | ParserConfigurationException | SAXException e) {
				throw new H2JFilterException(e);
			} finally {
				processor.setPath(currentPath);
			}
		}

		if (node != null) {
			parent.removeChild(node);
		}
	}

}
