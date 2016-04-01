package com.openfms.base.common;

import org.springframework.web.bind.annotation.RequestMethod;

import com.openfms.model.exceptions.ApiCallFailedException;

public class UnsupportedMethodException extends ApiCallFailedException {

	private static final long serialVersionUID = -5064031283523159944L;

	public UnsupportedMethodException(RequestMethod method) {
		super("API_CALL_FAILED_UNSUPPORTED_METHOD",null,method.toString());
	}

	
	
}
