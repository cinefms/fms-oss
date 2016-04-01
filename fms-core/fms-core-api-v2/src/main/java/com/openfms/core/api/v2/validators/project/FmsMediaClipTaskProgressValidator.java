package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.ErrorCodes;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMediaClipTaskService;
import com.openfms.core.service.project.FmsMediaClipTaskTypeService;
import com.openfms.model.Status;
import com.openfms.model.core.movie.FmsMediaClipTaskProgress;
import com.openfms.model.core.movie.FmsMediaClipTaskProgressFollowUp;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsMediaClipTaskProgressValidator extends AbstractValidator<FmsMediaClipTaskProgress> {

	@Autowired
	private CommonValidator commonValidator; 
	
	@Autowired
	private FmsMediaClipTaskService mediaClipTaskService;
	
	@Autowired
	private FmsMediaClipService mediaClipService;
	
	@Autowired
	private FmsMediaClipTaskTypeService taskTypeService;
	
	
	public FmsMediaClipTaskProgressValidator() {
		super(FmsMediaClipTaskProgress.class);
	}

	@Override
	public void validateInternal(FmsMediaClipTaskProgress target, Errors errors, Validator v) throws DatabaseException {
		if(target.getStatus()!=null) {
			if(target.getStatus().intValue()>Status.OK.value() || target.getStatus().intValue()<Status.NOT_APPLICABLE.value()) {
				errors.rejectValue("status", ErrorCodes.INVALID_VALUE);
			}
		}
		if(target.getFollowUps()!=null) {
			for(FmsMediaClipTaskProgressFollowUp s : target.getFollowUps()) {
				try {
					taskTypeService.get(s.getType());
				} catch (Exception e) {
					errors.rejectValue("followUps.type", ErrorCodes.INVALID_VALUE);
				}
			}
		}
		
	}
	
}
