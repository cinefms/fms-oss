package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.model.core.movie.FmsMovie;

@Component
public class FmsMovieValidator extends AbstractValidator<FmsMovie> {
	
	@Autowired
	private CommonValidator common;
	
	public FmsMovieValidator() {
		super(FmsMovie.class);
		setNonEmpty("name","externalId");
	}
	
	@Override
	public void validateInternal(FmsMovie target, Errors errors, Validator v) {
		if(target.getLanguageIds()!=null) {
			common.validateLanguage("languageIds", errors, target.getLanguageIds());
		}
		if(target.getCountryIds()!=null) {
			common.validateCountries("countryIds", errors, target.getCountryIds());
		}
	}

	
}
