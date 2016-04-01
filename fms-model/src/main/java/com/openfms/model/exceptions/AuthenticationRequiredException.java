package com.openfms.model.exceptions;


public class AuthenticationRequiredException extends BaseException {

	private static final long serialVersionUID = 370382719200851707L;

	public AuthenticationRequiredException() {
		super("AUTHENTICATION_REQUIRED_EXCEPTION",null);
	}

}
