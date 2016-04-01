package com.openfms.model.exceptions;

public class CreateFailedException extends OperationFailedException {

	private static final long serialVersionUID = -2205965972614511676L;

	public CreateFailedException(Class<?> c, Throwable cause) {
		super("CREATE_FAILED_EXCEPTION",cause,c);
	}
	
	
}
