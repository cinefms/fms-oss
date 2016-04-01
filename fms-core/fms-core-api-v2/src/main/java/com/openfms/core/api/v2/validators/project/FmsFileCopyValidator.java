package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsFileService;
import com.openfms.core.service.project.FmsMoviePackageCopyService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.model.core.movie.FmsFile;
import com.openfms.model.core.storage.FmsFileCopy;
import com.openfms.model.core.storage.FmsMoviePackageCopy;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsFileCopyValidator extends AbstractValidator<FmsFileCopy> {

	@Autowired
	private CommonValidator commonValidator;
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	@Autowired
	private FmsMoviePackageCopyService moviePackageCopyService;
	
	@Autowired
	private FmsFileService fileService;
	
	
	public FmsFileCopyValidator() {
		super(FmsFileCopy.class);
	}

	@Override
	public void validateInternal(FmsFileCopy target, Errors errors, Validator v) throws DatabaseException {
		try {
			moviePackageService.get(target.getPackageId());
		} catch (Exception e) {
			errors.rejectValue("packageId","invalid package");
		}
		try {
			FmsMoviePackageCopy pc = moviePackageCopyService.get(target.getPackageCopyId());
			if(pc.getMoviePackageId().compareTo(target.getPackageId())!=0) {
				errors.rejectValue("packageCopyId","invalid package copy: package copy exists, but it's not a copy of the package specified by packageId.");
			}
		} catch (Exception e) {
			errors.rejectValue("packageCopyId","invalid package copy");
		}
		try {
			FmsFile ff = fileService.get(target.getFileId());
			if(ff.getPackageId().compareTo(target.getPackageId())!=0) {
				errors.rejectValue("fileId","invalid file: file exists, but it's not part of the package specified by packageId.");
			}
		} catch (Exception e) {
			errors.rejectValue("fileId","invalid file");
		}
	}
	
}
