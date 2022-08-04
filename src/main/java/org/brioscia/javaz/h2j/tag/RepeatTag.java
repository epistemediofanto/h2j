package org.brioscia.javaz.h2j.tag;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.mw.Enviroments;
import org.brioscia.javaz.h2j.mw.LoopVar;
import org.brioscia.javaz.h2j.mw.Store.SetMode;
import org.brioscia.javaz.h2j.mw.XmlProcessor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RepeatTag extends BaseTag {

	public static final String REPEAT_LIST_MODE = "list";
	public static final String REPEAT_INDEX_MODE = "index";

	public void processNode(XmlProcessor processor, Node node) throws H2JFilterException {
		NamedNodeMap attributes;
		Node nodeValues, nodeVar, indexVar, sizeVar, rowVar, nodeMode;
		String nameVar, nameIndex, nameSize, nameRow;
		Node parent;
		String nameValues;
		Enviroments enviroments;
		SetMode mode;

		enviroments = processor.getEnviroments();

		parent = node.getParentNode();

		attributes = node.getAttributes();
		nodeValues = attributes.getNamedItem("values");
		assertNotNull(nodeValues, "missing 'values' attribute in 'repeat' tag");
		nameValues = nodeValues.getNodeValue();
		assertNotEmpty(nameValues, "missing values contents");

		nodeVar = attributes.getNamedItem("var");
		assertNotNull(nodeValues, "element var is missing");
		nameVar = nodeVar.getNodeValue();
		assertNotEmpty(nameVar, "missing var contents");

		indexVar = attributes.getNamedItem("index");
		nameIndex = indexVar != null ? indexVar.getNodeValue() : null;

		sizeVar = attributes.getNamedItem("size");
		nameSize = sizeVar != null ? sizeVar.getNodeValue() : null;

		rowVar = attributes.getNamedItem("row");
		nameRow = rowVar != null ? rowVar.getNodeValue() : null;

		nodeMode = attributes.getNamedItem("mode");
		mode = nodeMode != null ? REPEAT_INDEX_MODE.equals(nodeMode.getNodeValue()) ? SetMode.index : SetMode.list
				: SetMode.list;

		if (isEL(nameValues)) {
			MultiData items;
			Object oList;

			nameValues = nameValues.substring(2, nameValues.length() - 1);
			oList = enviroments.getObject(nameValues);

			items = new MultiData(oList, nameValues);
			if (items != null) {
				NodeList repeatChilds;
				Node repeatChild;

				repeatChilds = node.getChildNodes();
				try {
					int j = 0;
					Object item;
					if (nameIndex != null) {
						enviroments.push(nameSize, items.size());
					}

					enviroments.pushSetMode(mode);

					for (int ix = 0; ix < items.size(); ++ix) {
						item = items.get(ix);

						LoopVar var;
						if (processor.getEnviroments().getMode() == SetMode.list) {
							var = new LoopVar(nameVar, nameValues, item, ix);
						} else {
							var = new LoopVar(nameVar, nameValues + ".get(" + j + ")", item, ix);
						}
						enviroments.push(nameVar, var);

						if (nameRow != null) { // aggiungo all'enviroment il legame nome = numero riga (base 0)
							enviroments.push(nameRow, j);
						}

						++j;
						if (nameIndex != null) { // aggiungo all'enviroment il legame nome = numero riga (base 1)
							enviroments.push(nameIndex, j);
						}

						try {

							// Ciclo REPEAT
							for (int i = 0; i < repeatChilds.getLength(); ++i) {
								repeatChild = repeatChilds.item(i).cloneNode(true);
								parent.insertBefore(repeatChild, node);
								processor.processNode(repeatChild);
//								repeatChild.setUserData("h2j", "skip", null); // marcatore di tag creato dal framework, verra
																		// rimosso in valutazione
							}

						} finally {
							enviroments.pop(nameVar);
						}
						if (nameIndex != null) {
							enviroments.pop(nameIndex);
						}

					}
				} finally {		
					enviroments.popSetMode();
					enviroments.remove(nameVar);
					if (nameIndex != null) {
						enviroments.remove(nameIndex);
					}
					if (nameSize != null) {
						enviroments.remove(nameSize);
					}
				}
			}
		}
		parent.removeChild(node);
	}

	private class MultiData {
		private List<?> list;
		private Object array;
		private boolean isList = true;

		public MultiData(Object o, String str) throws H2JFilterException {
			if (o != null) {
				if (o instanceof List<?>) {
					this.isList = true;
					this.list = (List<?>) o;
				} else if (o.getClass().isArray()) {
					this.isList = false;
					this.array = o;
				} else {
					String type = "Found class " + o.getClass().getName() + " expected array or list in " + str + " or object not declared in scope";
					throw new H2JFilterException(type);
				}
			} else {
				this.list = new ArrayList<String>();
				this.isList = true;
			}
		}

		public int size() {
			return this.isList ? this.list.size() : Array.getLength(this.array);
		}

		public Object get(int ix) {
			return this.isList ? this.list.get(ix) : Array.get(this.array, ix);
		}
	}
}
