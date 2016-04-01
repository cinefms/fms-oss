package com.openfms.core.api.v2.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public interface FmsValidator {

	public abstract void validate(Object target, Errors errors, Validator validator);

	public abstract boolean supports(Class<?> clazz);

}
