package org.zaleuco.h2j.tag;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.Enviroments;

public abstract class BaseTag implements TagMap {

	protected static boolean isMapName(String mapName) {
		return (mapName != null) && mapName.startsWith("#{") && mapName.endsWith("}");
	}

	@Deprecated
	protected String trasforlELname(String htmlValue) {
		String value = null;
		if ((htmlValue.startsWith("#{")) && (htmlValue.endsWith("}"))) {
			value = htmlValue.substring(2, htmlValue.length() - 1);
		}
		return value;
	}

	protected void writeAttributes(Enviroments enviroments, Element element, NamedNodeMap attributes, String... ignore)
			throws H2JFilterException {
		String qualifiedName;
		String value;
		String name;
		String namespaceURI;

		for (int i = 0; i < attributes.getLength(); ++i) {
			qualifiedName = attributes.item(i).getNodeName();
			if (!in(qualifiedName, ignore)) {
				value = attributes.item(i).getNodeValue();
				namespaceURI = "";
				name = this.trasforlELname(value);
				if (name != null) {
					value = enviroments.getStringValue(name);
				}
				element.setAttributeNS(namespaceURI, qualifiedName, value);
			}
		}
	}

	protected static boolean in(String q, String... values) {
		boolean ignore = false;
		if (values != null) {
			for (String v : values) {
				if (q.equals(v)) {
					ignore = true;
					break;
				}
			}
		}
		return ignore;
	}

	protected void assertNotNull(Object o, String msg) throws H2JFilterException {
		if (o == null) {
			throw new H2JFilterException(msg);
		}
	}

	protected void assertNotEmpty(String v, String msg) throws H2JFilterException {
		if ((v == null) || (v.length() == 0)) {
			throw new H2JFilterException(msg);
		}
	}

	protected String evaluation(Enviroments enviroments, String exp) throws H2JFilterException {
		String value = exp;
		String newExp;
		newExp = this.trasforlELname(exp);
		if (newExp != null) {
			value = enviroments.getStringValue(newExp);
		}
		return value;
	}

}
