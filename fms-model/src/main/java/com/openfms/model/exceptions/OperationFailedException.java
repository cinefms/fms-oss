package com.openfms.model.exceptions;

public class OperationFailedException extends BaseException {

	private static final long serialVersionUID = 8384937540412763901L;

	protected OperationFailedException(String key, Throwable cause, Object... params) {
		super(key,cause,params);
	}

}
