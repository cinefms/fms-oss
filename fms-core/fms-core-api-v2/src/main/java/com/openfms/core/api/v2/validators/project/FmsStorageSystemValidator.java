package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.model.core.storage.FmsStorageSystem;

@Component
public class FmsStorageSystemValidator extends AbstractValidator<FmsStorageSystem> {
	
	@Autowired
	private CommonValidator common;
	
	public FmsStorageSystemValidator() {
		super(FmsStorageSystem.class);
	}
	
	@Override
	public void validateInternal(FmsStorageSystem target, Errors errors, Validator v) {
		common.validateLocation("locationId", errors, target.getLocationId());
	}

	
}
