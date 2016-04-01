package com.openfms.model.exceptions;


public class DatabaseException extends BaseException {
	
	private static final long serialVersionUID = -8922019746976969729L;

	public DatabaseException(Throwable cause) {
		super("DATABASE_EXCEPTION", cause);
	}

	protected DatabaseException(String key, Throwable cause, Object... params) {
		super(key,cause,params);
	}
	
	
	

}
