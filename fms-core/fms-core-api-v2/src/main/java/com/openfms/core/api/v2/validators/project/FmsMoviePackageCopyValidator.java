package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsMediaClipTypeService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.core.service.project.FmsStorageSystemService;
import com.openfms.model.core.storage.FmsMoviePackageCopy;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsMoviePackageCopyValidator extends AbstractValidator<FmsMoviePackageCopy> {

	@Autowired
	private CommonValidator commonValidator;
	
	@Autowired
	private FmsMediaClipTypeService mediaClipTypeService;
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	@Autowired
	private FmsStorageSystemService storageSystemService;
	
	
	public FmsMoviePackageCopyValidator() {
		super(FmsMoviePackageCopy.class);
	}

	@Override
	public void validateInternal(FmsMoviePackageCopy target, Errors errors, Validator v) throws DatabaseException {
		try {
			moviePackageService.get(target.getMoviePackageId());
		} catch (Exception e) {
			errors.rejectValue("moviePackageId","invalid package");
		}
		try {
			storageSystemService.get(target.getStorageSystemId());
		} catch (Exception e) {
			errors.rejectValue("storageSystemId","invalid storage system");
		}
	}
	
}
