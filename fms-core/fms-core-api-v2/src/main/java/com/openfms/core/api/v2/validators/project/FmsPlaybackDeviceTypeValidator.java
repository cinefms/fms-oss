package com.openfms.core.api.v2.validators.project;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.core.playback.FmsPlaybackDeviceType;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsPlaybackDeviceTypeValidator extends AbstractValidator<FmsPlaybackDeviceType>{

	public FmsPlaybackDeviceTypeValidator() {
		super(FmsPlaybackDeviceType.class);
		setNonEmpty("name");
	}

	@Override
	public void validateInternal(FmsPlaybackDeviceType target, Errors errors, Validator v) throws DatabaseException {
	}
	
}