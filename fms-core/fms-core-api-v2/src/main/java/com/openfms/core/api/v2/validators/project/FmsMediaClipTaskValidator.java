package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.ErrorCodes;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsMediaClipTaskTypeService;
import com.openfms.model.core.movie.FmsMediaClipTask;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsMediaClipTaskValidator extends AbstractValidator<FmsMediaClipTask> {

	@Autowired
	private CommonValidator commonValidator; 
	
	@Autowired
	private FmsMediaClipTaskTypeService mediaTaskTypeService;
	
	
	public FmsMediaClipTaskValidator() {
		super(FmsMediaClipTask.class);
	}

	@Override
	public void validateInternal(FmsMediaClipTask target, Errors errors, Validator v) throws DatabaseException {
		if(target.getType() == null) {
			errors.rejectValue("type", ErrorCodes.INVALID_VALUE, "cannot be empty");
		} else {
			try {
				mediaTaskTypeService.get(target.getType());
			} catch (Exception e) {
				errors.rejectValue("type", ErrorCodes.INVALID_VALUE, "cannot be empty");
			}
		}
		commonValidator.validateMediaClip("mediaClipId", errors, target.getMediaClipId());
	}
	
}
