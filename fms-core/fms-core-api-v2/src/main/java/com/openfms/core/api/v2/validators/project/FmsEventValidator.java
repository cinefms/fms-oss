package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsEventValidator extends AbstractValidator<FmsEvent> {

	@Autowired
	private CommonValidator commonValidator; 
	
	public FmsEventValidator() {
		super(FmsEvent.class);
	}

	@Override
	public void validateInternal(FmsEvent target, Errors errors, Validator v) throws DatabaseException {
		commonValidator.validateLocation("locationId", errors, target.getLocationId());
		commonValidator.validateTags("tags", target.getTags(), errors);
		v.validate(target.getEventItems(), errors);
	}
	
}
