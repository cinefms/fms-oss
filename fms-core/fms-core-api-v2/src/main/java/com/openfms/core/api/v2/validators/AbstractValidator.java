package com.openfms.core.api.v2.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.openfms.model.exceptions.DatabaseException;

public abstract class AbstractValidator<T> implements FmsValidator {
	
	private Class<T> t;
	private String[] nonEmpty;
	
	public AbstractValidator(Class<T> t) {
		this.t = t;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		if(clazz == t) {
			return true;
		}
		return false;
	}
	
	public abstract void validateInternal(T target, Errors errors, Validator v) throws DatabaseException;
	
	@SuppressWarnings("unchecked")
	@Override
	public void validate(Object target, Errors errors, Validator v) {
		if(nonEmpty != null) {
			for(String s : nonEmpty) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, s, ErrorCodes.CANNOT_BE_EMPTY, s+" cannot be null or empty");
			}
		}
		try {
			validateInternal((T)target,errors, v);
		} catch (Exception e) {
			errors.reject("VALIDATION_FAILED",e.getMessage());
		}
		
	}

	public void setNonEmpty(String... nonEmpty) {
		this.nonEmpty = nonEmpty;
	}
	
	
}
