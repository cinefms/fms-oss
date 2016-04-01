package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.model.core.movie.FmsAudioFormat;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsAudioFormatValidator extends AbstractValidator<FmsAudioFormat> {

	@Autowired
	private CommonValidator commonValidator;
	
	public FmsAudioFormatValidator() {
		super(FmsAudioFormat.class);
		setNonEmpty("name");
	}

	@Override
	public void validateInternal(FmsAudioFormat target, Errors errors, Validator v) throws DatabaseException {
	}
	
}
