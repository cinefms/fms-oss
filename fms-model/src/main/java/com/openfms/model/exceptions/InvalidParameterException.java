package com.openfms.model.exceptions;


public abstract class InvalidParameterException extends BaseException {

	private static final long serialVersionUID = 4415813205391204326L;

	protected InvalidParameterException(String key, Exception e, Object... params) {
		super(key,e,params);
	}
	
}
