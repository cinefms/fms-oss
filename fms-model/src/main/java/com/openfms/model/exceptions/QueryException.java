package com.openfms.model.exceptions;

import org.springframework.context.MessageSource;

import com.openfms.utils.common.text.MessageLoader;

public class QueryException extends BaseRuntimeException {

	private static final long serialVersionUID = -8253845789639622850L;

	private static MessageSource messageSource = MessageLoader.getMessageSource(QueryException.class);
	
	public QueryException(String key) {
		this(messageSource,key,null);
	}
	
	public QueryException(MessageSource messageSource, String key, Throwable cause) {
		super(messageSource, key, cause);
	}


}
