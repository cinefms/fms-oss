package com.openfms.model.exceptions;

public class RequestedEntityTooLargeException extends BaseException {

	private static final long serialVersionUID = 3224081192251209898L;

	public RequestedEntityTooLargeException(int size, int limit) {
		super("REQUESTED_ENTITY_TOO_LARGE", null, size, limit);
	}

}
