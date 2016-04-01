package com.openfms.core.api.v2.validators.project;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.model.core.FmsObject;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsObjectValidator extends AbstractValidator<FmsObject> {

	public FmsObjectValidator() {
		super(FmsObject.class);
	}

	@Override
	public void validateInternal(FmsObject target, Errors errors, Validator v) throws DatabaseException {
		if(target.getId()!=null && !target.getId().matches("^[a-zA-Z0-9-_.]*$")) {
			errors.rejectValue("id", "invalid characters in ID");
		}
	}

	
}
