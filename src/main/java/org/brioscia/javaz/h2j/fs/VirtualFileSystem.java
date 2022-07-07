package org.brioscia.javaz.h2j.fs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.brioscia.javaz.h2j.filter.H2JProcessorFilter;
import org.brioscia.javaz.h2j.mw.Enviroments;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class VirtualFileSystem {

	public static boolean validating = true;
	private HashMap<String, Document> cache;
	private ServletContext context;

	public VirtualFileSystem(ServletContext context) {
		this.cache = new HashMap<String, Document>();
		this.context = context;
	}

	public Document loadDocument(String filename) throws ParserConfigurationException, SAXException, IOException {
		Document doc = this.cache.get(filename);
		if (doc == null) {
			FileInputStream fis = new FileInputStream(this.context.getRealPath(filename));
			doc = load(fis);
			fis.close();
			if (Enviroments.enableCacheFile) {
				try {
					this.cache.put(filename, cloneDocument(doc));
					Enviroments.trace("%s: stored file in cache", filename);
				} catch (TransformerException e) {
					Enviroments.error(e, "critical error cannot cache document: %s", e.getMessage());
					this.cache.put(filename, null);
				}
			} else {
				Enviroments.debug("%s: file not in cache", filename);
			}
		} else {
			try {
				doc = cloneDocument(doc);
				Enviroments.trace("read %s from cache", filename);
			} catch (TransformerException e) {
				Enviroments.error(e, "clone cache document error: %s", e.getMessage());
				this.cache.put(filename, null);
				return this.loadDocument(filename);
			}
		}
//		if (Enviroments.trace) {
//			printDocument(doc);
//		}
		return doc;
	}

	private static Document cloneDocument(Document originalDocument) throws TransformerException {
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer tx = tfactory.newTransformer();
		DOMSource source = new DOMSource(originalDocument);
		DOMResult result = new DOMResult();
		tx.transform(source, result);
		return (Document) result.getNode();
	}

	private static Document load(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory;
		DocumentBuilder dBuilder;

		dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(validating);
		dbFactory.setNamespaceAware(true);
		dBuilder = dbFactory.newDocumentBuilder();
		if (!validating) {
			dBuilder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String arg0, String arg1) throws SAXException, IOException {
					return new InputSource(new StringReader(""));
				}
			});
		}

		return dBuilder.parse(is, H2JProcessorFilter.XHTML_ECODE);
	}

//	private static void printDocument(Document doc) {
//		try {
//			TransformerFactory tf = TransformerFactory.newInstance();
//			Transformer transformer = tf.newTransformer();
//			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
//
//			transformer.transform(new DOMSource(doc), new StreamResult(System.err));
//		} catch (Exception e) {
//			Enviroments.trace(e, "dump documento fail %s", e.getMessage());
//		}
//	}
}
