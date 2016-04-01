package com.openfms.model.exceptions;

public class DuplicateValueException extends InvalidParameterException {

	private static final long serialVersionUID = -2020312134664296546L;

	public DuplicateValueException(String field, String value) {
		super("DUPLICATE_VALUE_EXCEPTION",null, field, value);
	}
	
	
}
