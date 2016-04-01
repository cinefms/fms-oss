package com.openfms.model.exceptions;


public class AccessDeniedException extends BaseException {

	private static final long serialVersionUID = 3435801871235764416L;
	
	public AccessDeniedException() {
		super("ACCESS_DENIED", null);
	}
	
	public AccessDeniedException(Class<?> clazz, String id) {
		super("ACCESS_DENIED_OBJ_TYPE_ID", null, clazz, id);
	}
	
	public AccessDeniedException(Class<?> clazz) {
		super("ACCESS_DENIED_OBJ_TYPE", null, clazz);
	}
	
	public AccessDeniedException(String key, Throwable cause, Object... params) {
		super(key, cause, params);
	}
	

}
