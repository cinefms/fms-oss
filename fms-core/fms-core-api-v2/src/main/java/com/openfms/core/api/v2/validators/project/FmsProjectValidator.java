package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.ErrorCodes;
import com.openfms.core.service.admin.ProjectService;
import com.openfms.model.core.project.FmsProject;

@Component
public class FmsProjectValidator extends AbstractValidator<FmsProject> {
	
	@Autowired
	private ProjectService projectService;

	public FmsProjectValidator() {
		super(FmsProject.class);
		setNonEmpty("name", "shortName");
	}
	
	@Override
	public void validateInternal(FmsProject target, Errors errors, Validator v) {
		validateHostnames(target,errors,v);
	}

	public void validateHostnames(FmsProject target, Errors errors, Validator v) {
		if(target.getHostnames()!=null) {
			for(String s : target.getHostnames()) {
				if(s==null) {
					errors.rejectValue(ErrorCodes.CANNOT_BE_EMPTY,"hostname");
				} else if(s.trim().length()==0){
					errors.rejectValue(ErrorCodes.CANNOT_BE_EMPTY,"hostname");
				}
				try {
					FmsProject p = projectService.getProjectForHost(s);
					if(p!=null && (p.getId()+"").compareTo(target.getId()+"")!=0) {
						errors.rejectValue("INVALID_VALUE", "");
					}
				} catch (Exception e) {
					errors.reject("VALIDATION_FAILED",e.getMessage());
				}
			}
		}
	}
	
}
