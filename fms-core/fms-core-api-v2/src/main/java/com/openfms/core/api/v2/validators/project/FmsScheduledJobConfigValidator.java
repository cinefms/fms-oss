package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobConfig;

@Component
public class FmsScheduledJobConfigValidator extends AbstractValidator<FmsScheduledJobConfig> {
	
	@Autowired
	private CommonValidator common;
	
	public FmsScheduledJobConfigValidator() {
		super(FmsScheduledJobConfig.class);
		setNonEmpty("name");
	}
	
	@Override
	public void validateInternal(FmsScheduledJobConfig target, Errors errors, Validator v) {
	}

	
}
