package com.openfms.core.service.project.impl.listeners.setup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.openfms.model.core.global.FmsCountry;

@Component
public class CountryDefaults extends AbstractDefaults<FmsCountry> {

	public static Log log = LogFactory.getLog(CountryDefaults.class);

	public CountryDefaults() {
		log.info(" #### DEFAULTS: "+this.getClass());
	}
	
	@Override
	public Class<FmsCountry> getDefaultType() {
		return FmsCountry.class;
	}
	
	@Override
	public String getFilename() {
		return "countries.json";
	}

}
