package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsMediaClipTypeService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.core.service.project.FmsMoviePackageTypeService;
import com.openfms.model.core.movie.FmsMoviePackage;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsMoviePackageValidator extends AbstractValidator<FmsMoviePackage> {

	@Autowired
	private CommonValidator commonValidator;
	
	@Autowired
	private FmsMediaClipTypeService mediaClipTypeService;
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	@Autowired
	private FmsMoviePackageTypeService moviePackageTypeService;
	
	
	public FmsMoviePackageValidator() {
		super(FmsMoviePackage.class);
		setNonEmpty("externalId","movieId");
	}

	@Override
	public void validateInternal(FmsMoviePackage target, Errors errors, Validator v) throws DatabaseException {
		commonValidator.validateMovie("movieId", errors, target.getMovieId());
		try {
			moviePackageTypeService.get(target.getType());
		} catch (Exception e) {
			errors.rejectValue("type","invalid package type");
		}
	}
	
}
