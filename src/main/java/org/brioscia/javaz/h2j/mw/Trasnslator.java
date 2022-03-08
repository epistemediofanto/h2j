package org.brioscia.javaz.h2j.mw;

import java.util.HashMap;

import org.brioscia.javaz.h2j.filter.H2JFilterException;
import org.brioscia.javaz.h2j.tag.ATag;
import org.brioscia.javaz.h2j.tag.AliasTag;
import org.brioscia.javaz.h2j.tag.ButtonTag;
import org.brioscia.javaz.h2j.tag.DeclareTag;
import org.brioscia.javaz.h2j.tag.DefaultH2JTag;
import org.brioscia.javaz.h2j.tag.DefaultHtmlTag;
import org.brioscia.javaz.h2j.tag.FormTag;
import org.brioscia.javaz.h2j.tag.IfTag;
import org.brioscia.javaz.h2j.tag.IncludeIfTag;
import org.brioscia.javaz.h2j.tag.IncludeTag;
import org.brioscia.javaz.h2j.tag.InputTag;
import org.brioscia.javaz.h2j.tag.JsonDataTag;
import org.brioscia.javaz.h2j.tag.OptionsTag;
import org.brioscia.javaz.h2j.tag.OutTag;
import org.brioscia.javaz.h2j.tag.RepeatTag;
import org.brioscia.javaz.h2j.tag.SelectTag;
import org.brioscia.javaz.h2j.tag.SwitchTag;
import org.brioscia.javaz.h2j.tag.TagMap;
import org.brioscia.javaz.h2j.tag.TextAreaTag;

public class Trasnslator {

	private static Trasnslator instance;
	private HashMap<String, TagMap> tags;
	private TagMap defaultH2JTag;
	private TagMap defaultHtmlTag;

	public static void init() throws H2JFilterException {
		instance = new Trasnslator();
	}

	public static Trasnslator getInstance() {
		return instance;
	}

	private Trasnslator() throws H2JFilterException {
		TagMap tagMap;

		this.defaultH2JTag = new DefaultH2JTag();
		this.tags = new HashMap<String, TagMap>();

		this.defaultHtmlTag = new DefaultHtmlTag();
		this.tags = new HashMap<String, TagMap>();

		tagMap = new ATag();
		this.registerTag("a", tagMap);

		tagMap = new AliasTag();
		this.registerTag("alias", tagMap);

		tagMap = new ButtonTag();
		this.registerTag("button", tagMap);

		tagMap = new DeclareTag();
		this.registerTag("declare", tagMap);

		tagMap = new FormTag();
		this.registerTag("form", tagMap);

		tagMap = new IfTag();
		this.registerTag("if", tagMap);

		tagMap = new IncludeTag();
		this.registerTag("include", tagMap);

		tagMap = new IncludeIfTag();
		this.registerTag("includeif", tagMap);

		tagMap = new InputTag();
		this.registerTag("input", tagMap);

		tagMap = new JsonDataTag();
		this.registerTag("jsondata", tagMap);

		tagMap = new OptionsTag();
		this.registerTag("options", tagMap);

		tagMap = new OutTag();
		this.registerTag("out", tagMap);

		tagMap = new RepeatTag();
		this.registerTag("repeat", tagMap);

		tagMap = new SelectTag();
		this.registerTag("select", tagMap);

		tagMap = new SwitchTag();
		this.registerTag("switch", tagMap);

		tagMap = new TextAreaTag();
		this.registerTag("textarea", tagMap);
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
