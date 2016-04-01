package com.openfms.model.exceptions;

public class InvalidProjectException extends DatabaseException {

	private static final long serialVersionUID = -436673093929343668L;

	public InvalidProjectException(String thisProjectId, String otherProjectId) {
		super("INVALID_PROJECT_EXCEPTION",null,thisProjectId,otherProjectId);
	}
	
}
