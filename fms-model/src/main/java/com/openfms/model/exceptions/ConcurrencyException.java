package com.openfms.model.exceptions;

public class ConcurrencyException extends BaseException {

	private static final long serialVersionUID = 8421167257130449906L;

	public ConcurrencyException(String key, Throwable cause, Object... params) {
		super(key, cause, params);
	}

	public ConcurrencyException(String key, Throwable cause) {
		super(key, cause);
	}

	public ConcurrencyException(Throwable cause) {
		super(cause);
	}

	public ConcurrencyException() {
		super(null);
	}
	
}
