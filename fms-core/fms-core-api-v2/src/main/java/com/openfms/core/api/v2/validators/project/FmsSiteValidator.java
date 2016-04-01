package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsMediaClipTypeService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.model.core.playback.FmsSite;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsSiteValidator extends AbstractValidator<FmsSite> {

	@Autowired
	private CommonValidator commonValidator;
	
	@Autowired
	private FmsMediaClipTypeService mediaClipTypeService;
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	
	public FmsSiteValidator() {
		super(FmsSite.class);
		setNonEmpty("name");
	}

	@Override
	public void validateInternal(FmsSite target, Errors errors, Validator v) throws DatabaseException {
	}
	
}
