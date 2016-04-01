package com.openfms.core.api.v2.validators.project;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.model.core.project.FmsMailTemplate;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsMailTemplateValidator extends AbstractValidator<FmsMailTemplate> {

	public FmsMailTemplateValidator() {
		super(FmsMailTemplate.class);
		setNonEmpty("name", "subject", "body");
	}

	@Override
	public void validateInternal(FmsMailTemplate target, Errors errors, Validator v) throws DatabaseException {
	}

}
