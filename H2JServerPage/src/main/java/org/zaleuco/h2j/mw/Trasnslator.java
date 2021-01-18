package org.zaleuco.h2j.mw;

import java.util.HashMap;

import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.tag.DefaultH2JTag;
import org.zaleuco.h2j.tag.DefaultHtmlTag;
import org.zaleuco.h2j.tag.FormTag;
import org.zaleuco.h2j.tag.AnchorTag;
import org.zaleuco.h2j.tag.ButtonTag;
import org.zaleuco.h2j.tag.IncludeTag;
import org.zaleuco.h2j.tag.OptionsTag;
import org.zaleuco.h2j.tag.OutTag;
import org.zaleuco.h2j.tag.RepeatTag;
import org.zaleuco.h2j.tag.TagMap;

public class Trasnslator {

	private HashMap<String, TagMap> tags;
	private TagMap defaultH2JTag;
	private TagMap defaultHtmlTag;

	public Trasnslator(Enviroments enviroments) throws H2JFilterException {
		TagMap tagMap;

		this.defaultH2JTag = new DefaultH2JTag();
		this.defaultH2JTag.init(enviroments);
		this.tags = new HashMap<String, TagMap>();

		this.defaultHtmlTag = new DefaultHtmlTag();
		this.defaultHtmlTag.init(enviroments);
		this.tags = new HashMap<String, TagMap>();

		tagMap = new AnchorTag();
		tagMap.init(enviroments);
		this.registerTag("a", tagMap);

		tagMap = new ButtonTag();
		tagMap.init(enviroments);
		this.registerTag("button", tagMap);
		
		tagMap = new FormTag();
		tagMap.init(enviroments);
		this.registerTag("form", tagMap);

		tagMap = new IncludeTag();
		tagMap.init(enviroments);
		this.registerTag("include", tagMap);

		tagMap = new OptionsTag();
		tagMap.init(enviroments);
		this.registerTag("options", tagMap);

		tagMap = new OutTag();
		tagMap.init(enviroments);
		this.registerTag("out", tagMap);

		tagMap = new RepeatTag();
		tagMap.init(enviroments);
		this.registerTag("repeat", tagMap);

	}

	public TagMap getTag(String fullName) {
		TagMap map;
		map = this.tags.get(fullName);
		return map == null ? this.defaultH2JTag : map;
	}

	public TagMap getDefaultHTML() {
		return this.defaultHtmlTag;
	}

	public TagMap registerTag(String name, TagMap map) {
		TagMap old;
		old = this.tags.get(name);
		this.tags.put(name, map);
		return old;
	}

	public TagMap unregisterTag(String name) {
		TagMap old;
		old = this.tags.get(name);
		if (old != null) {
			this.tags.remove(name);
		}
		return old;
	}
}
