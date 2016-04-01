package com.openfms.model.exceptions;

public class NotAuthorizedException extends AccessDeniedException {

	public NotAuthorizedException() {
		super();
	}

	public NotAuthorizedException(Class<?> clazz, String id) {
		super(clazz, id);
	}

	public NotAuthorizedException(Class<?> clazz) {
		super(clazz);
	}

	public NotAuthorizedException(String key, Throwable cause, Object... params) {
		super(key, cause, params);
	}

	public static final long serialVersionUID = 4463325760321363444L;
	
	

}
