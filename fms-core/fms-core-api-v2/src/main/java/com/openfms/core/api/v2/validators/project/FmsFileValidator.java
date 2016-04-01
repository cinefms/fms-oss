package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsMediaClipTypeService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.model.core.movie.FmsFile;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsFileValidator extends AbstractValidator<FmsFile> {

	@Autowired
	private CommonValidator commonValidator;
	
	@Autowired
	private FmsMediaClipTypeService mediaClipTypeService;
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	
	public FmsFileValidator() {
		super(FmsFile.class);
		setNonEmpty("type","path");
	}

	@Override
	public void validateInternal(FmsFile target, Errors errors, Validator v) throws DatabaseException {
		try {
			moviePackageService.get(target.getPackageId());
		} catch (Exception e) {
			errors.rejectValue("packageId","invalied package id");
		}
	}
	
}
