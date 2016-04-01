package com.openfms.model.exceptions;

public class MissingParameterException extends InvalidParameterException {
	
	private static final long serialVersionUID = 1L;

	public MissingParameterException(String fieldname) {
		super("MISSING_PARAMETER_EXCEPTION",null,fieldname);
	}
	

}
