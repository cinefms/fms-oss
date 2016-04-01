package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsAudioFormatService;
import com.openfms.core.service.project.FmsFileService;
import com.openfms.core.service.project.FmsFramerateService;
import com.openfms.core.service.project.FmsMediaAspectService;
import com.openfms.core.service.project.FmsMediaClipTypeService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.core.service.project.FmsScreenAspectService;
import com.openfms.model.core.media.FmsMovieReel;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Component
public class FmsMediaClipValidator extends AbstractValidator<FmsMediaClip> {

	@Autowired
	private CommonValidator commonValidator;
	
	@Autowired
	private FmsMediaClipTypeService mediaClipTypeService;
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	@Autowired
	private FmsMediaAspectService mediaAspectService;
	
	@Autowired
	private FmsScreenAspectService screenAspectService;
	
	@Autowired
	private FmsFramerateService frameratesService;
	
	@Autowired
	private FmsAudioFormatService audioFormatService;
	
	@Autowired
	private FmsFileService fileService;
	
	
	public FmsMediaClipValidator() {
		super(FmsMediaClip.class);
	}

	@Override
	public void validateInternal(FmsMediaClip target, Errors errors, Validator v) throws DatabaseException {
		
		try {
			for(FmsMovieReel reel : target.getReels()) {
				v.validate(reel, errors);
			}
		} catch (Exception e) {
			errors.rejectValue("reels","one or more invalid reels");
		}
		try {
			for(String s : target.getMoviePackageIds()) {
				moviePackageService.get(s);
			}
		} catch (Exception e) {
			errors.rejectValue("moviePackageIds","invalid package id");
		}
		try {
			mediaClipTypeService.get(target.getType());
		} catch (Exception e) {
			errors.rejectValue("type","invalid media clip type");
		}
		try {
			mediaAspectService.get(target.getMediaAspect());
		} catch (Exception e) {
			errors.rejectValue("mediaAspect","invalid media aspect");
		}
		try {
			screenAspectService.get(target.getScreenAspect());
		} catch (Exception e) {
			errors.rejectValue("screenAspect","invalid screen aspect");
		}
		try {
			frameratesService.get(target.getFps());
		} catch (Exception e) {
			errors.rejectValue("FPS","invalid frame rate");
		}
		try {
			audioFormatService.get(target.getAudioFormat());
		} catch (Exception e) {
			errors.rejectValue("audioFormat","invalid audio format");
		}
		try {
			if(target.getFileIds()!=null) {
				for(String fileId : target.getFileIds()) {
					try {
						fileService.get(fileId);
					} catch (EntityNotFoundException enfe) {
						errors.rejectValue("fileIds","invalid file id");
					}
				}
			}
		} catch (Exception e) {
			errors.rejectValue("fileIds","invalid file ids");
		}
		commonValidator.validateLanguage("audioLanguageIds", errors, target.getAudioLanguageIds());
		commonValidator.validateLanguage("subtitleLanguageIds", errors, target.getSubtitleLanguageIds());
	}
	
}
