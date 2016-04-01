package com.openfms.model.exceptions;


public class JsonDecodeException extends BaseException {

	private static final long serialVersionUID = -5949820343683587898L;

	public JsonDecodeException(Throwable cause) {
		super("JSON_DECODE_EXCEPTION",cause);
	}

}
