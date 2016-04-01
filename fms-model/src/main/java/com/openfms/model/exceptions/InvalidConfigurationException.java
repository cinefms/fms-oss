package com.openfms.model.exceptions;

import org.springframework.context.MessageSource;

import com.openfms.utils.common.text.MessageLoader;

public class InvalidConfigurationException extends BaseRuntimeException {

	
	private static final long serialVersionUID = 1012819623918445147L;

	public static final String INVALID_CONFIGURATION_GENERAL = "INVALID_CONFIGURATION_GENERAL";

	private static MessageSource messageSource = MessageLoader.getMessageSource(InvalidConfigurationException.class);

	public InvalidConfigurationException(String key, Throwable cause, String details) {
		super(messageSource, key, cause, details);
	}
	
	
	

}
