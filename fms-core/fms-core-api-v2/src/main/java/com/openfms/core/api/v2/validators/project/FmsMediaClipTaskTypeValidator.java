package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.ErrorCodes;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsMediaClipTaskTypeService;
import com.openfms.model.core.movie.FmsMediaClipTaskType;
import com.openfms.model.core.movie.FmsMediaClipTaskTypeNext;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsMediaClipTaskTypeValidator extends AbstractValidator<FmsMediaClipTaskType> {

	@Autowired
	private CommonValidator commonValidator; 
	
	@Autowired
	private FmsMediaClipTaskTypeService mediaTaskTypeService;
	
	
	public FmsMediaClipTaskTypeValidator() {
		super(FmsMediaClipTaskType.class);
		setNonEmpty("name");
	}

	@Override
	public void validateInternal(FmsMediaClipTaskType target, Errors errors, Validator v) throws DatabaseException {
		for(String s : target.getGenerateForMediaClipTypes()) {
			commonValidator.validateMediaClipType("generateForMediaClipTypes",s,errors);
		}
		for(String s : target.getEnableForMediaClipTypes()) {
			commonValidator.validateMediaClipType("enableForMediaClipTypes",s,errors);
		}
		for(FmsMediaClipTaskTypeNext next : target.getNext()) {
			if(next.getNextTaskType() != null) {
				try {
					mediaTaskTypeService.get(next.getNextTaskType());
				} catch (Exception e) {
					errors.rejectValue("type", ErrorCodes.INVALID_VALUE, "cannot be empty");
				}
			}
			if(next.getAddTag() != null) {
				commonValidator.validateTag("next.addTag", next.getAddTag(), errors);
			}
		}
	}
	
}
