package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.core.service.project.FmsSiteService;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsLocationValidator extends AbstractValidator<FmsLocation> {

	@Autowired
	private CommonValidator commonValidator;
	
	@Autowired
	private FmsSiteService siteService;
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	
	public FmsLocationValidator() {
		super(FmsLocation.class);
		setNonEmpty("name");
	}

	@Override
	public void validateInternal(FmsLocation target, Errors errors, Validator v) throws DatabaseException {
		commonValidator.validateTags("tags", target.getTags(), errors);
		try {
			siteService.get(target.getSiteId());
		} catch (Exception e) {
			errors.rejectValue("siteId","invalid site id");
		}
	}
	
}
