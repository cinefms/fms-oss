package com.openfms.model.exceptions;

public class DependencyException extends OperationFailedException {

	private static final long serialVersionUID = 1637421937242046787L;

	public DependencyException(Class<?> clazz, String id) {
		super("DEPENDENCY_EXCEPTION",null, clazz, id);
	}

}
