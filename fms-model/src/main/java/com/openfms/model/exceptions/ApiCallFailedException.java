package com.openfms.model.exceptions;

public class ApiCallFailedException extends BaseException {

	private static final long serialVersionUID = 1106696866245697050L;

	public ApiCallFailedException(int status, Exception e) {
		super("API_FALL_FAILED_EXCEPTION",e,status);
	}

	protected ApiCallFailedException(String key, Throwable cause, Object... params) {
		super(key,cause,params);
	}
	

}
