package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.core.service.project.FmsPlaybackDeviceService;
import com.openfms.model.core.crypto.FmsKeyRequest;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsKeyRequestValidator extends AbstractValidator<FmsKeyRequest> {

	@Autowired
	private CommonValidator commonValidator;
	
	@Autowired
	private FmsMediaClipService mediaClipService;
	
	@Autowired
	private FmsMovieService movieService;
	
	@Autowired
	private FmsPlaybackDeviceService playbackDeviceService;
	
	
	
	public FmsKeyRequestValidator() {
		super(FmsKeyRequest.class);
		setNonEmpty("reason");
	}
	
	@Override
	public void validateInternal(FmsKeyRequest target, Errors errors, Validator v) throws DatabaseException {
		if(target.getStart()==null) {
			errors.reject("start");
		}
		if(target.getEnd()==null) {
			errors.reject("end");
		}
		if(target.getStart()!=null && target.getEnd()!=null && target.getStart().after(target.getEnd())) {
			errors.reject("end","end is before start");
		}
		if(target.getMovieId()==null) {
			errors.reject("movieId","movieId has to be set");
		} else {
			try {
				movieService.get(target.getMovieId());
			} catch (Exception e) {
				errors.reject("movieId","movieId is invalid");
			}
		}
		if(target.getMediaClipId()==null) {
			errors.reject("mediaClipId","mediaClipId has to be set");
		} else {
			try {
				mediaClipService.get(target.getMediaClipId());
			} catch (Exception e) {
				errors.reject("mediaClipId","mediaClipId is invalid");
			}
		}
		if(target.getDeviceId()==null) {
			errors.reject("deviceId","deviceId has to be set");
		} else {
			try {
				playbackDeviceService.get(target.getDeviceId());
			} catch (Exception e) {
				errors.reject("deviceId","deviceId is invalid");
			}
		}
		
	}
	
}
