package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.project.common.CommonValidator;
import com.openfms.core.service.project.FmsCryptoCertificateService;
import com.openfms.core.service.project.FmsMediaClipTypeService;
import com.openfms.core.service.project.FmsPlaybackDeviceTypeService;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsPlaybackDeviceValidator extends AbstractValidator<FmsPlaybackDevice> {

	@Autowired
	private CommonValidator commonValidator;
	
	@Autowired
	private FmsMediaClipTypeService mediaClipTypeService;
	
	@Autowired
	private FmsCryptoCertificateService certificateService;
	
	
	@Autowired
	private FmsPlaybackDeviceTypeService playbackDeviceTypeService;
	
	
	public FmsPlaybackDeviceValidator() {
		super(FmsPlaybackDevice.class);
		setNonEmpty("name");
	}

	@Override
	public void validateInternal(FmsPlaybackDevice target, Errors errors, Validator v) throws DatabaseException {
		commonValidator.validateLocation("locationId", errors, target.getLocationId());
		try {
			if(target.getCertificateId()!=null) {
				certificateService.get(target.getCertificateId());
			}
		} catch (Exception e) {
			errors.rejectValue("certificateIds","one or more invalid certificates");
		}
		try {
			if(target.getDeviceType()!=null) {
				playbackDeviceTypeService.get(target.getDeviceType());
			}
		} catch (Exception e) {
			errors.rejectValue("deviceType","device type is not valid");
		}
		
	}
	
	
}
