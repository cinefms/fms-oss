package com.openfms.core.service.project.impl.listeners.setup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.openfms.model.core.movie.FmsFramerate;

@Component
public class FramerateDefaults extends AbstractDefaults<FmsFramerate> {

	public static Log log = LogFactory.getLog(FramerateDefaults.class);

	public FramerateDefaults() {
		log.info(" #### DEFAULTS: "+this.getClass());
	}
	
	@Override
	public Class<FmsFramerate> getDefaultType() {
		return FmsFramerate.class;
	}
	
	@Override
	public String getFilename() {
		return "framerates.json";
	}

}
