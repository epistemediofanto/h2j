package org.brioscia.javaz.h2j.tag;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.Node;

public interface TagMap {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException;
}
