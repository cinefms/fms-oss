package com.openfms.core.service.project.impl.listeners.setup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.openfms.model.core.global.FmsLanguage;

@Component
public class LanguageDefaults extends AbstractDefaults<FmsLanguage> {

	public static Log log = LogFactory.getLog(LanguageDefaults.class);

	public LanguageDefaults() {
		log.info(" #### DEFAULTS: "+this.getClass());
	}
	
	@Override
	public Class<FmsLanguage> getDefaultType() {
		return FmsLanguage.class;
	}
	
	@Override
	public String getFilename() {
		return "languages.json";
	}

}
