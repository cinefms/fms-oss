package com.openfms.model.exceptions;


public class VersioningException extends BaseException {

	private static final long serialVersionUID = 615028814058971965L;

	public VersioningException(String clazz, String objectId, Long oldVersion, Long newVersion) {
		super("VERSIONING_EXCEPTION", null, clazz, objectId, oldVersion, newVersion);
	}
	
}
