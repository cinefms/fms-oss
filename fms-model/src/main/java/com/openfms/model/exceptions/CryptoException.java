package com.openfms.model.exceptions;

public class CryptoException extends BaseException {

	private static final long serialVersionUID = 5150318406559428207L;

	public CryptoException(String operation, Throwable cause) {
		super("CRYPTO_EXCEPTION",cause,operation);
	}

	
	
	
	
}
