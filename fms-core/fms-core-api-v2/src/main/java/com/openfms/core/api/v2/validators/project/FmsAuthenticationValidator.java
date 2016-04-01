package com.openfms.core.api.v2.validators.project;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.model.core.auth.FmsAuthentication;
import com.openfms.model.exceptions.DatabaseException;

@Component
public class FmsAuthenticationValidator extends AbstractValidator<FmsAuthentication> {

	public FmsAuthenticationValidator() {
		super(FmsAuthentication.class);
	}

	@Override
	public void validateInternal(FmsAuthentication target, Errors errors, Validator v) throws DatabaseException {
		if(target.getEmailAddress()!=null && target.getPassword()!=null) {
			// ok
		} else if(target.getUsername()!=null && target.getPassword()!=null) {
			// ok
		} else if(target.getUsername()!=null) {
			// ok
		} else if(target.getEmailAddress()!=null) {
			// ok
		} else if(target.getNewPassword()!=null && target.getNewPasswordToken()!=null) {
			// ok
		} else if(target.getUsername()!=null && target.getNewPassword()!=null && target.getPassword()!=null) {
			// ok
		} else {
			errors.reject("not a valid combination ... ");
		}
		
	}

}
