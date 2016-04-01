package com.openfms.core.api.v2.validators.project;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.model.core.global.FmsTag;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsTagValidator extends AbstractValidator<FmsTag> {

	public FmsTagValidator() {
		super(FmsTag.class);
		setNonEmpty("name");
	}

	@Override
	public void validateInternal(FmsTag target, Errors errors, Validator v) throws DatabaseException {
		
	}

}
