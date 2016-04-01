package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.service.admin.ProjectService;
import com.openfms.model.core.global.FmsCountry;

@Component
public class FmsCountryValidator extends AbstractValidator<FmsCountry> {
	
	@Autowired
	private ProjectService projectService;

	public FmsCountryValidator() {
		super(FmsCountry.class);
		setNonEmpty("name", "code");
	}
	
	@Override
	public void validateInternal(FmsCountry target, Errors errors, Validator v) {
	}
	
}
