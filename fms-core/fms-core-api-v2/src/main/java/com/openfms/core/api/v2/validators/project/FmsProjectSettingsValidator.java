package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.model.core.project.FmsProjectSettings;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsProjectSettingsValidator extends AbstractValidator<FmsProjectSettings> {

	@Autowired
	private CommonValidator commonValidator; 
	
	public FmsProjectSettingsValidator() {
		super(FmsProjectSettings.class);
	}

	@Override
	public void validateInternal(FmsProjectSettings target, Errors errors, Validator v) throws DatabaseException {
	}
	
}
