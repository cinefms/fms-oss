package com.openfms.core.api.v2.validators.project;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.ErrorCodes;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsMediaClipTypeService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.core.service.project.FmsProjectGroupService;
import com.openfms.model.core.auth.FmsProjectUser;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsProjectUserValidator extends AbstractValidator<FmsProjectUser> {

	@Autowired
	private CommonValidator commonValidator;
	
	@Autowired
	private FmsMediaClipTypeService mediaClipTypeService;
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	@Autowired
	private FmsProjectGroupService groupService;
	
	
	public FmsProjectUserValidator() {
		super(FmsProjectUser.class);
		setNonEmpty("username","name", "email");
	}

	@Override
	public void validateInternal(FmsProjectUser target, Errors errors, Validator v) throws DatabaseException {
		if(!target.getUsername().matches("^[a-zA-Z0-9_]*$")) {
			errors.rejectValue("username", ErrorCodes.INVALID_VALUE, "usernames can only contain a-z, A-Z, 0-9 and the underscore ('_') character");
		}
		
		for(String movieId : target.getMovieIds()) {
			commonValidator.validateMovie("movieIds", errors, movieId);
		}
		for(String groupId : target.getGroups()) {
			try {
				groupService.get(groupId);
			} catch (Exception e) {
				errors.rejectValue("groups","invalid group id ("+e.getMessage()+")");
			}
		}
		
		try {
			if(!EmailValidator.getInstance().isValid(target.getEmail())) {
				errors.rejectValue("email","invalid email address");
			}
		} catch (Exception e) {
			errors.rejectValue("email","invalid email");
		}
	}
	
}
