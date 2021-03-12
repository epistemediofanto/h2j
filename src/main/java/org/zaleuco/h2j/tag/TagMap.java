package org.zaleuco.h2j.tag;

import org.w3c.dom.Node;
import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.XmlProcessor;

public interface TagMap {

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException;
}
