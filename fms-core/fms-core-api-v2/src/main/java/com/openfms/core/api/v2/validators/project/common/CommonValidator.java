package com.openfms.core.api.v2.validators.project.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.openfms.core.api.v2.validators.ErrorCodes;
import com.openfms.core.service.project.FmsCountryService;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsLanguageService;
import com.openfms.core.service.project.FmsLocationService;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMediaClipTypeService;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.core.service.project.FmsTagService;

@Component
public class CommonValidator {

	@Autowired
	private FmsTagService tagService;
	@Autowired
	private FmsLanguageService languageService;
	@Autowired
	private FmsCountryService countryService;
	@Autowired
	private FmsMovieService movieService;
	@Autowired
	private FmsMovieVersionService movieVersionService;
	@Autowired
	private FmsMediaClipService mediaClipService;
	@Autowired
	private FmsMediaClipTypeService mediaClipTypeService;
	@Autowired
	private FmsEventService eventService;
	@Autowired
	private FmsLocationService locationService;

	
	public void validateTag(String fieldname, String tag, Errors errors) {
		try {
			tagService.get(tag);
		} catch (Exception e) {
			errors.rejectValue(fieldname,"invalid value");
		}
	}
	
	public void validateTags(String fieldname, List<String> tags, Errors errors) {
		if(tags==null || tags.size()==0) {
			return;
		}
		try {
			for(String s : tags) {
				validateTag(fieldname, s, errors);
			}
		} catch (Exception e) {
			errors.reject(ErrorCodes.INVALID_VALUE,"invalid tag");
		}
	}
	
	public void validateLanguage(String fieldname, Errors errors, List<String> languageIds) {
		try {
			for(String s : languageIds) {
				languageService.get(s);
			}
		} catch (Exception e) {
			errors.reject(ErrorCodes.INVALID_VALUE,"invalid language");
		}
	}

	public void validateCountries(String fieldname, Errors errors, List<String> countryIds) {
		try {
			for(String s : countryIds) {
				countryService.get(s);
			}
		} catch (Exception e) {
			errors.reject(ErrorCodes.INVALID_VALUE,"invalid country");
		}
	}
	
	public void validateMovie(String fieldname, Errors errors, String movieId) {
		try {
			movieService.get(movieId);
		} catch (Exception e) {
			errors.reject(ErrorCodes.INVALID_VALUE,"invalid movieId");
		}
	}
	
	public void validateMovieVersion(String fieldname, Errors errors, String movieVersionId) {
		try {
			movieVersionService.get(movieVersionId);
		} catch (Exception e) {
			errors.reject(ErrorCodes.INVALID_VALUE,"invalid movie version id");
		}
	}
	
	
	public void validateLocation(String fieldname, Errors errors, String locationId) {
		try {
			locationService.get(locationId);
		} catch (Exception e) {
			errors.reject(ErrorCodes.INVALID_VALUE,"invalid location id");
		}
	}
	
	
	public void validateMediaClip(String fieldname, Errors errors, String mediaClipId) {
		try {
			mediaClipService.get(mediaClipId);
		} catch (Exception e) {
			errors.reject(ErrorCodes.INVALID_VALUE,"invalid media clip id");
		}
	}
	
	public void validateMediaClip(String fieldname, Errors errors, List<String> mediaClipIds) {
		try {
			for(String s : mediaClipIds) {
				validateMediaClip(fieldname, errors, s);
			}
		} catch (Exception e) {
			errors.reject(ErrorCodes.INVALID_VALUE,"invalid media clip id");
		}
	}

	public void validateMediaClipType(String fieldname, String mediaClipTypeId, Errors errors) {
		try {
			mediaClipTypeService.get(mediaClipTypeId);
		} catch (Exception e) {
			errors.reject(ErrorCodes.INVALID_VALUE,"invalid media clip type id");
		}
	}
	
	
	
}
