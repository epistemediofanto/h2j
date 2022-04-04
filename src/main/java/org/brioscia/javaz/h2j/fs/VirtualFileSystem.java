package org.brioscia.javaz.h2j.fs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.print.Doc;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.brioscia.javaz.h2j.filter.H2JProcessorFilter;
import org.brioscia.javaz.h2j.mw.Enviroments;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class VirtualFileSystem {

	private HashMap<String, Document> fileCache;
	private ServletContext context;

	public VirtualFileSystem(ServletContext context) {
		this.fileCache = new HashMap<String, Document>();
		this.context = context;
	}

	public Document loadDocument(String filename) throws ParserConfigurationException, SAXException, IOException {
		Document doc = this.fileCache.get(filename);
		if (doc == null) {
			InputStream is = load(filename);
			doc = load(is);
			is.close();
			if (Enviroments.enableCacheFile) {
				this.fileCache.put(filename, doc);
			} else {
				System.out.println("laod " + filename);
			}
		}
		return doc;
	}

	@Deprecated
	public static Document load(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory;
		DocumentBuilder dBuilder;

		dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		dBuilder = dbFactory.newDocumentBuilder();

		return dBuilder.parse(is, H2JProcessorFilter.XHTML_ECODE);
	}

	@Deprecated
	public InputStream load(String filename) throws FileNotFoundException, IOException {
		byte[] stream;
		stream = this.readFile(filename);
		return new ByteArrayInputStream(stream);
	}

	private byte[] readFile(String filename) throws FileNotFoundException, IOException {
		FileInputStream fis;
		byte[] buffer;
		File file;
		int size;
		int r, off;

		file = new File(this.context.getRealPath(filename));
		size = (int) file.length();
		buffer = new byte[size];

		off = 0;
		fis = new FileInputStream(file);
		try {
			while (((r = fis.read(buffer, off, size)) != -1) && (size > 0)) {
				off += r;
				size -= r;
			}
		} finally {
			fis.close();
		}

		return buffer;
	}

}
