package com.openfms.core.service.project.impl.listeners.setup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.openfms.model.core.movie.FmsScreenAspect;

@Component
public class ScreenAspectDefaults extends AbstractDefaults<FmsScreenAspect> {

	public static Log log = LogFactory.getLog(ScreenAspectDefaults.class);

	public ScreenAspectDefaults() {
		log.info(" #### DEFAULTS: "+this.getClass());
	}
	
	@Override
	public Class<FmsScreenAspect> getDefaultType() {
		return FmsScreenAspect.class;
	}
	
	@Override
	public String getFilename() {
		return "screen_aspects.json";
	}


}
