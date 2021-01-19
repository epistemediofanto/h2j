package org.zaleuco.h2j.mw;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.tag.TagMap;

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

	public Document load(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory;
		DocumentBuilder dBuilder;

		dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		dBuilder = dbFactory.newDocumentBuilder();

		return dBuilder.parse(is);
	}

	public void process(InputStream is, OutputStream os) throws H2JFilterException {
		Document doc;
		TransformerFactory transformerFactory;
		Transformer transformer;
		DOMSource source;

		try {
			doc = load(is);

			this.processNode(doc.getDocumentElement());

			source = new DOMSource(doc);

			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "html");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(source, new StreamResult(os));

		} catch (IOException | TransformerException | ParserConfigurationException | SAXException e) {
			throw new H2JFilterException(e);
		}

	}

	public void processNode(Node node) throws H2JFilterException {
		String localName;
		String namespace;
		TagMap tagMap;

		if ("skip".equals(node.getUserData("h2j"))) {
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
