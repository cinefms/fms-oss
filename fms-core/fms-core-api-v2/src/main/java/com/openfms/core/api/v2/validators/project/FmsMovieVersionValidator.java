package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsLanguageService;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.model.core.movie.FmsMovieVersion;

@Component
public class FmsMovieVersionValidator extends AbstractValidator<FmsMovieVersion> {
	
	@Autowired
	private FmsMovieVersionService movieVersionService;
	@Autowired
	private FmsMovieService movieService;
	@Autowired
	private FmsLanguageService languageService;
	@Autowired
	private FmsMediaClipService mediaClipService;

	@Autowired
	private CommonValidator commonValidator;
	
	public FmsMovieVersionValidator() {
		super(FmsMovieVersion.class);
		setNonEmpty("movieId", "externalId");
	}
	
	@Override
	public void validateInternal(FmsMovieVersion target, Errors errors, Validator v) {
		commonValidator.validateTags("tags", target.getTags(), errors);
		commonValidator.validateMovie("movieId", errors, target.getMovieId());
		commonValidator.validateLanguage("audioLanguageIds", errors, target.getAudioLanguageIds());
		commonValidator.validateLanguage("subtitleLanguageIds", errors, target.getSubtitleLanguageIds());
		commonValidator.validateMediaClip("mediaClipIds", errors, target.getMediaClipIds());
	}

	
}
