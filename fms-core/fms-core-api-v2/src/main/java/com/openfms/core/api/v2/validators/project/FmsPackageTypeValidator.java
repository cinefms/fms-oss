package com.openfms.core.api.v2.validators.project;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.model.core.movie.FmsMoviePackageType;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsPackageTypeValidator extends AbstractValidator<FmsMoviePackageType> {

	
	
	public FmsPackageTypeValidator() {
		super(FmsMoviePackageType.class);
		setNonEmpty("name");
	}

	@Override
	public void validateInternal(FmsMoviePackageType target, Errors errors, Validator v) throws DatabaseException {
	}
	
	
}
