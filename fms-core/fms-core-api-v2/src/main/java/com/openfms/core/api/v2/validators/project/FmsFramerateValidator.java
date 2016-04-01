package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.model.core.movie.FmsFramerate;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsFramerateValidator extends AbstractValidator<FmsFramerate> {

	@Autowired
	private CommonValidator commonValidator;
	
	public FmsFramerateValidator() {
		super(FmsFramerate.class);
		setNonEmpty("name");
	}

	@Override
	public void validateInternal(FmsFramerate target, Errors errors, Validator v) throws DatabaseException {
	}
	
}
