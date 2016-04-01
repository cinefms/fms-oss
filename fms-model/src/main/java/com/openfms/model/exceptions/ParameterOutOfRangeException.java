package com.openfms.model.exceptions;

public class ParameterOutOfRangeException extends InvalidParameterException {

	private static final long serialVersionUID = 7662563110468894872L;

	public ParameterOutOfRangeException(String field, Object value) {
		super("PARAMETER_OUT_OF_RANGE_EXCEPTION_SIMPLE",null, field, value);
	}

	
}
