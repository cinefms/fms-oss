package com.openfms.model.exceptions;

public class AuthenticationFailedException extends AccessDeniedException {

	public AuthenticationFailedException() {
		super();
	}

	public AuthenticationFailedException(Class<?> clazz, String id) {
		super(clazz, id);
	}

	public AuthenticationFailedException(Class<?> clazz) {
		super(clazz);
	}
	
	public AuthenticationFailedException(String key, Throwable cause, Object... params) {
		super(key, cause, params);
	}

	private static final long serialVersionUID = 4463325760321363444L;
	
	

}
