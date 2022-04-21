package org.brioscia.javaz.h2j.mw;

import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.brioscia.javaz.expression.LexicalParser;
import org.brioscia.javaz.expression.NodeToken;
import org.brioscia.javaz.expression.SyntaxError;
import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.filter.H2JProcessorFilter;
import org.brioscia.javaz.h2j.tag.TagMap;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlProcessor {

	public static final String NAMESPACE = "http://java.h2j.org";
	private String page;
	private String path;
	private Enviroments enviroments;
	private Trasnslator trasnslator;

	public XmlProcessor(Enviroments enviroments, String page) {
		this.page = page;
		this.path = this.getPath(page);
		this.enviroments = enviroments;
		this.trasnslator = Trasnslator.getInstance();
	}

	public void process(Document doc, OutputStream os) throws H2JFilterException {
		TransformerFactory transformerFactory;
		Transformer transformer;
		DOMSource source;

		try {

			this.processNode(doc.getDocumentElement());

			source = new DOMSource(doc);

			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "html");
			transformer.setOutputProperty(OutputKeys.INDENT, H2JProcessorFilter.XHTML_INDENT);
			transformer.setOutputProperty(OutputKeys.ENCODING, H2JProcessorFilter.XHTML_ECODE);

			transformer.transform(source, new StreamResult(os));

		} catch (TransformerException e) {
			throw new H2JFilterException(e);
		}

	}

	public void processNode(Node node) throws H2JFilterException {
		String localName;
		String namespace;
		TagMap tagMap;

		if ("skip".equals(node.getUserData("h2j"))) { // i tag creati dal framework non sono da processare
			node.setUserData("h2j", null, null);
		} else {

			localName = node.getLocalName();
			namespace = node.getNamespaceURI();

			if (NAMESPACE.equals(namespace)) {
				tagMap = this.trasnslator.getTag(localName);
			} else {
				tagMap = this.trasnslator.getDefaultHTML();
			}
			tagMap.processNode(this, node);
		}
	}

	public String resolveLoopVar(String element) throws H2JFilterException {
		NodeToken node;
		try {
			node = LexicalParser.process(element);
			this.replaceName(node);

			element = node.toString();
		} catch (SyntaxError e) {
			throw new H2JFilterException(e);
		}
		return element;
	}

	private void replaceName(NodeToken node) {
		LoopVar loopVar;

		loopVar = this.getEnviroments().getLoopVar(node.getValue());
		if (loopVar != null) {
			node.setValue(loopVar.getFullName());
		}
		while ((node != null) && (node.getChilds().size() > 0)) {
			node = node.getChilds().get(0);
			if (node != null) {
				if (node.getType() == NodeToken.Type.name) {
					continue;
				} else if (node.getType() == NodeToken.Type.function) {
					for (int i = 0; i < node.getChilds().size(); ++i) {
						replaceName(node.getChilds().get(i));
					}
				} else if (node.getType() == NodeToken.Type.arrayIndex) {
					for (int i = 0; i < node.getChilds().size(); ++i) {
						replaceName(node.getChilds().get(i));
					}
				}
			}
		}
	}

	public String getPage() {
		return page;
	}

	private String getPath(String page) {
		int pos;

		pos = page.lastIndexOf('/');
		if (pos != -1) {
			page = page.substring(0, pos + 1);
		}
		return page;
	}

	public String getPath() {
		return path;
	}

	public Enviroments getEnviroments() {
		return enviroments;
	}

}
