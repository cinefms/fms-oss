package com.openfms.core.api.v2.validators.project;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.model.core.movie.FmsMediaClipType;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsMediaClipTypeValidator extends AbstractValidator<FmsMediaClipType>{

	public FmsMediaClipTypeValidator() {
		super(FmsMediaClipType.class);
		setNonEmpty("name");
	}

	@Override
	public void validateInternal(FmsMediaClipType target, Errors errors, Validator v) throws DatabaseException {
	}
	
}
