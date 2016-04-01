package com.openfms.utils.common.text.i18n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Language {
	
	private String name;
	private String code;
	private List<String> codes = new ArrayList<String>();
	private Map<String,String> names = new HashMap<String, String>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName(Locale locale) {
		String out = null;
		if(locale!=null && locale.getLanguage()!=null) {
			out = names.get(locale.getLanguage());
		}
		if(out == null) {
			out = getName();
		}
		return name;
	}
	
	public String getName(Language language) {
		String out = null;
		if(language.getCode()!=null) {
			out = names.get(language.getCode());
		}
		if(out == null) {
			out = getName();
		}
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getCodes() {
		return Collections.unmodifiableList(codes);
	}
	
	public void addCode(String code) {
		if(!codes.contains(code)) {
			codes.add(code);
		}
	}
	
	public void removeCode(String code) {
		codes.remove(code);
	}
	
	public void addName(String code, String name) {
		names.put(code, name);
	}
	
	
	
}
