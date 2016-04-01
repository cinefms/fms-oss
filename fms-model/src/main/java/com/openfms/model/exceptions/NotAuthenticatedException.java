package com.openfms.model.exceptions;

public class NotAuthenticatedException extends AccessDeniedException {

	public NotAuthenticatedException() {
		super();
	}

	public NotAuthenticatedException(Class<?> clazz, String id) {
		super(clazz, id);
	}

	public NotAuthenticatedException(Class<?> clazz) {
		super(clazz);
	}

	public NotAuthenticatedException(String key, Throwable cause, Object... params) {
		super(key, cause, params);
	}

	public static final long serialVersionUID = 4463325760321363444L;
	
	

}
