package com.openfms.core.service.project.impl.listeners.setup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.openfms.model.core.movie.FmsMediaAspect;

@Component
public class MediaAspectDefaults extends AbstractDefaults<FmsMediaAspect> {

	public static Log log = LogFactory.getLog(MediaAspectDefaults.class);

	
	public MediaAspectDefaults() {
		log.info(" #### DEFAULTS: "+this.getClass());
	}
	
	@Override
	public Class<FmsMediaAspect> getDefaultType() {
		return FmsMediaAspect.class;
	}
	
	@Override
	public String getFilename() {
		return "media_aspects.json";
	}


}
