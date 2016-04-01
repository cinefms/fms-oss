package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.model.core.movie.FmsMediaAspect;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsMediaAspectValidator extends AbstractValidator<FmsMediaAspect> {

	@Autowired
	private CommonValidator commonValidator;
	
	public FmsMediaAspectValidator() {
		super(FmsMediaAspect.class);
		setNonEmpty("name");
	}

	@Override
	public void validateInternal(FmsMediaAspect target, Errors errors, Validator v) throws DatabaseException {
	}
	
}
