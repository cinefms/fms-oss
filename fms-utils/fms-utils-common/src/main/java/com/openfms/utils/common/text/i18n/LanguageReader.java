package com.openfms.utils.common.text.i18n;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LanguageReader {
	
	private static HashMap<String, List<Language>> lByCode = new HashMap<String, List<Language>>();
	private static HashMap<String, Language> lByISO = new HashMap<String, Language>();
	private static List<Language> languages;
	
	private static void init() {
		if(languages!=null) {
			return;
		}
		BufferedReader br = null;
		InputStream is = null;
		languages = new ArrayList<Language>();
		try {
			is = LanguageReader.class.getResourceAsStream("languages");
			br = new BufferedReader(new InputStreamReader(is));
			String s = null;
			while((s = br.readLine())!=null) {
				Language l = new Language();
				String[] x = s.split("\\t");
				for(int i=0;i<4;i++) {
					if(x[i].trim().length()>0) {
						l.addCode(x[i].trim());
					}
				}
				String code = l.getCodes().get(0);
				for(String sx : l.getCodes()) {
					if(sx.length() < code.length()) {
						code = sx;
					}
				}
				l.setCode(code);
				l.setName(x[6].trim());
				l.addName("en",x[6].trim());
				languages.add(l);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e2) {
			}
			try {
				is.close();
			} catch (Exception e2) {
			}
		}
		
	}
	
	public static List<String> getIso639_3Codes() {
		init();
		return null;
	}
	
	public static Language getLanguage(String iso639_3) {
		init();
		Language out = lByISO.get(iso639_3);
		if(out==null) {
			for(Language l : getLanguages()) {
				if(l.getCode().compareTo(iso639_3)==0) {
					out = l;
					break;
				}
			}
			lByISO.put(iso639_3, out);
		}
		return out;
	}
	
	public static List<Language> getLanguages(String code) {
		init();
		List<Language> out = lByCode.get(code);
		if(out==null) {
			out = new ArrayList<Language>();
			for(Language l : getLanguages()) {
				if(l.getCodes().contains(code)) {
					out.add(l);
				}
			}
			lByCode.put(code, out);
		}
		return out;
	}

	public static List<Language> getLanguages() {
		init();
		return Collections.unmodifiableList(languages);
	}

	public static void main(String[] args) {
		for(Language l : getLanguages("zh")) {
			System.err.println(l.getName()+": "+l.getCodes());
		}
		{
			Language l = getLanguage("eng");
			System.err.println(l.getName()+": "+l.getCodes());
		}
		System.err.println(getLanguages().size()+" languages");
	}
	

}
