package com.openfms.core.service.project.impl.listeners.setup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.openfms.model.core.movie.FmsAudioFormat;

@Component
public class AudioFormatDefaults extends AbstractDefaults<FmsAudioFormat> {

	public static Log log = LogFactory.getLog(AudioFormatDefaults.class);

	public AudioFormatDefaults() {
		log.info(" #### DEFAULTS: "+this.getClass());
	}
	
	@Override
	public Class<FmsAudioFormat> getDefaultType() {
		return FmsAudioFormat.class;
	}
	
	@Override
	public String getFilename() {
		return "audio_formats.json";
	}

}
