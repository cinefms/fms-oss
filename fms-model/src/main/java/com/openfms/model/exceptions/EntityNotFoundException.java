package com.openfms.model.exceptions;


public class EntityNotFoundException extends BaseException {

	private static final long serialVersionUID = 7402905847246456000L;

	public EntityNotFoundException(Class<?> type, String id) {
		super("ENTITY_NOT_FOUND",null, type.getCanonicalName(), id);
	}

	public EntityNotFoundException(Class<?> type, String id, Exception e) {
		super("ENTITY_NOT_FOUND",e, type.getCanonicalName(), id);
	}

	
}
