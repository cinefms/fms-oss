package com.openfms.utils.common.text;

import java.util.ArrayList;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class MessageLoader {

	private static MessageSource getMessageSource(String[] basepaths) {
		ReloadableResourceBundleMessageSource out = new ReloadableResourceBundleMessageSource();
		out.setBasenames(basepaths);
		out.setDefaultEncoding("UTF-8");
		return out;
	}
	
	public static MessageSource getMessageSource(Class<?> clazz) {
		return getMessageSource(clazz, "messages");
	}

	public static MessageSource getMessageSource(Class<?> clazz, String name) {
		StringBuffer sb = new StringBuffer();
		ArrayList<String> ss = new ArrayList<String>();
		for(String s: clazz.getCanonicalName().split("\\.")) {
			sb.append(sb.length()>0?"/":"");
			sb.append(s);
			ss.add(0,sb.toString()+"/"+name);
		}
		ss.add(0,clazz.getCanonicalName().replace('.', '/'));
		String[] basenames = new String[ss.size()];
		ss.toArray(basenames);
		return getMessageSource(basenames);
	}
	
	
}
