package com.openfms.model.exceptions;


public class AuthorizationFailedException extends BaseException {

	private static final long serialVersionUID = 370382719200851707L;

	public AuthorizationFailedException() {
		super("AUTHORIZATION_FAILED_EXCEPTION",null);
	}

}
