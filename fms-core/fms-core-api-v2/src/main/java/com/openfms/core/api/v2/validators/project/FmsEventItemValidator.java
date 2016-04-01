package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.model.core.playback.FmsEventItem;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsEventItemValidator extends AbstractValidator<FmsEventItem> {

	@Autowired
	private CommonValidator commonValidator; 
	
	public FmsEventItemValidator() {
		super(FmsEventItem.class);
	}

	@Override
	public void validateInternal(FmsEventItem target, Errors errors, Validator v) throws DatabaseException {
		commonValidator.validateTags("tags", target.getTags(), errors);
		if(target.getMovieId()!=null) {
			commonValidator.validateMovie("movieId", errors, target.getMovieId());
		}
		if(target.getMovieVersionId()!=null) {
			commonValidator.validateMovieVersion("movieVersionId", errors, target.getMovieVersionId());
		}
	}
	
}
