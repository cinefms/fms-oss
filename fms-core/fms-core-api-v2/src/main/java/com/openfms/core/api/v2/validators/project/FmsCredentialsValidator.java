package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.model.core.config.FmsCredentials;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsCredentialsValidator extends AbstractValidator<FmsCredentials> {

	@Autowired
	private CommonValidator commonValidator; 
	
	public FmsCredentialsValidator() {
		super(FmsCredentials.class);
	}

	@Override
	public void validateInternal(FmsCredentials target, Errors errors, Validator v) throws DatabaseException {
	}
	
}
