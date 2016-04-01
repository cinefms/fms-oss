package com.openfms.model.exceptions;

public class DeleteOperationFailedException extends OperationFailedException {

	private static final long serialVersionUID = -3673923818244224656L;

	public DeleteOperationFailedException(Class<?> c, String id, Throwable cause) {
		super("DELETE_FAILED_EXCEPTION",cause,c,id);
	}

}
