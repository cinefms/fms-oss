package com.openfms.model.exceptions;

public class InvalidReferenceException extends InvalidParameterException {

	private static final long serialVersionUID = -2020312134664296546L;

	public InvalidReferenceException(Class<?> clazz, String id, Exception e) {
		super("INVALID_REFERENCE_EXCEPTION",e,clazz,id);
	}

	public InvalidReferenceException(String field, String type) {
		super("INVALID_REFERENCE_EXCEPTION",null,field,type);
	}
	
	
}
