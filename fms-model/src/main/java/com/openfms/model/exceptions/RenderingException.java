package com.openfms.model.exceptions;

public class RenderingException extends OperationFailedException {

	private static final long serialVersionUID = -4150521018129256658L;
	
	public RenderingException(String template, Throwable cause) {
		super("RENDERING_FAILED_EXCEPTION",cause);
	}

}
