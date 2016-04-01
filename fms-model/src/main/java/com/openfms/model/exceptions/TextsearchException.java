package com.openfms.model.exceptions;


public class TextsearchException extends BaseException {

	private static final long serialVersionUID = -8989150649689529085L;
	
	public TextsearchException(Throwable cause) {
		super("TEXTSEARCH_EXCEPTION", cause);
	}

}
