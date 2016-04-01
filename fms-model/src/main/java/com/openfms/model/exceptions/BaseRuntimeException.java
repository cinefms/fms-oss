package com.openfms.model.exceptions;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.openfms.utils.common.text.MessageLoader;

public class BaseRuntimeException extends RuntimeException implements FmsException {

	private static final long serialVersionUID = -2518491440965789397L;
	
	private static MessageSource defaultMessageSource = MessageLoader.getMessageSource(BaseRuntimeException.class);
	
	private MessageSource messageSource = defaultMessageSource;
	private String key;
	private Object[] params;

	public BaseRuntimeException(String key, Throwable cause) {
		this(defaultMessageSource,key,cause);
	}

	public BaseRuntimeException(MessageSource messageSource, String key, Throwable cause) {
		this(messageSource,key,cause,new Object[0]);
	}
	
	public BaseRuntimeException(MessageSource messageSource, String key, Throwable cause, Object... params) {
		this.setKey(key);
		this.messageSource = messageSource;
		this.params = params;
	}
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public Locale getLocale() {
		return LocaleContextHolder.getLocale();
	}
	
	@Override
	public String getMessage() {
		return super.getLocalizedMessage();
	}
	
	public String getLocalizedMessage() {
		try {
			return getMessageSource().getMessage(getKey(), params, getLocale());
		} catch (Exception e) {
			return "Unknown Exception ("+e.getClass().getCanonicalName()+":"+e.getMessage()+")";
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	} 
	
	

}
