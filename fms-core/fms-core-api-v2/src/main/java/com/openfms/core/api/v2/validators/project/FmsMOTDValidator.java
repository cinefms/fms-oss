package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.service.admin.ProjectService;
import com.openfms.model.core.global.FmsMOTD;

@Component
public class FmsMOTDValidator extends AbstractValidator<FmsMOTD> {
	
	@Autowired
	private ProjectService projectService;

	public FmsMOTDValidator() {
		super(FmsMOTD.class);
		setNonEmpty("title", "message");
	}
	
	@Override
	public void validateInternal(FmsMOTD target, Errors errors, Validator v) {
		
	}

	
}
