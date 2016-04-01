package com.openfms.utils.common.freemarker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerUtil {
	
	public static String process(String in, Map<String,Object> model) throws IOException {
		Template t = getTemplate(in);
		return freemarkerDo(model, t);
	}
	
	private static String freemarkerDo(Object model, Template t) throws IOException {
		try {
			if(t==null) {
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(baos);
			t.process(model, osw);
			osw.flush();
			osw.close();
			return new String(baos.toByteArray(), "utf-8");
		} catch (Exception e) {
			throw new IOException("error proecessing template",e);
		}
	}

	
	private static Template getTemplate(String template) throws IOException {
		if(template == null) {
			return null;
		}
		try {
			Configuration freemarkerCfg = new Configuration();
			StringTemplateLoader tl = new StringTemplateLoader();
			freemarkerCfg.setURLEscapingCharset("utf-8");
			freemarkerCfg.setTemplateLoader(tl);
			tl.putTemplate("a", template);
			return freemarkerCfg.getTemplate("a");
		} catch (Exception e) {
			throw new IOException("error creating template from string",e);
		}
	}
	

}
