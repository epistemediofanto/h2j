package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.expression.InvokerException;
import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.Enviroments;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class BaseTag implements TagMap {

	/**
	 * verifica se si tratta di una espressione da valutare, ovvero se la stringa è
	 * racchiusa tra #{ }
	 * 
	 * @param mapName parametro sul quale valutare se si tratta o meno di una espressione 
	 * @return restituisce true se mapName è una espressione da valutare in forma #{stringa}
	 */
	protected static boolean isEL(String mapName) {
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

	protected void processNodes(XmlProcessor processor, NodeList nodeList) throws H2JFilterException {
		if (nodeList != null) {
			Node n;
			short s;
			for (int i = 0; i < nodeList.getLength(); ++i) {
				n = nodeList.item(i);
				s = n.getNodeType();
				if (s == Node.ELEMENT_NODE) {
					processor.processNode(nodeList.item(i));
				}
			}
		}
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

	protected void assertNotTrue(boolean bExp, String msg) throws H2JFilterException {
		if (bExp) {
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

	protected static boolean checkRoot(String el) {
		return ((el != null) && (el.startsWith(Enviroments.ROOT_VAR)));
	}

	/**
	 * se la string inizia con ROOT viene sostituito il suo valore, es ROOT/path e
	 * ROOT=inizio, restituisce inizio/path
	 * 
	 * @deprecated sostituire con #{ ROOT + exp }
	 * 
	 * @param processor processatore dell'espressione value
	 * @param value     stringa da valutare
	 * @return restituisce la valutazione del parametro value
	 * @throws H2JFilterException viene sollevata se non è stato possibile valutare il parametro value
	 */
	@Deprecated
	public String valueRoot(XmlProcessor processor, String value) throws H2JFilterException {
		String out = value;
		if (checkRoot(value)) {
			Object oValue;
			try {
				oValue = processor.getEnviroments().get(Enviroments.ROOT_VAR);
				out = value.substring(Enviroments.ROOT_VAR.length()) + (oValue != null ? oValue.toString() : "");
			} catch (InvokerException e) {
				throw new H2JFilterException(e);
			}
		}
		return out;
	}
}
