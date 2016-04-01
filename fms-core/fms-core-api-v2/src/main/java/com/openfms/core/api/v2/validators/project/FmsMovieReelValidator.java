package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.model.core.media.FmsMovieReel;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsMovieReelValidator extends AbstractValidator<FmsMovieReel> {

	@Autowired
	private CommonValidator commonValidator;
	
	public FmsMovieReelValidator() {
		super(FmsMovieReel.class);
	}

	@Override
	public void validateInternal(FmsMovieReel target, Errors errors, Validator v) throws DatabaseException {
		commonValidator.validateLanguage("audioLanguageIds", errors, target.getAudioLanguageIds());
		commonValidator.validateLanguage("subtitleLanguageIds", errors, target.getSubtitleLanguageIds());
	}
	
}
