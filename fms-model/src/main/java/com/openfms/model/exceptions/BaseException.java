package com.openfms.model.exceptions;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.openfms.utils.common.text.MessageLoader;

public class BaseException extends Exception implements FmsException {

	private static Log log = LogFactory.getLog(BaseException.class);
	
	private static final long serialVersionUID = -2518491440965789397L;
	
	private static MessageSource defaultMessageSource = MessageLoader.getMessageSource(BaseException.class);
	
	private MessageSource messageSource = defaultMessageSource;
	private String key;
	private Object[] params;

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(String key, Throwable cause) {
		this(key,cause,new Object[0]);
	}
	
	public BaseException(String key, Throwable cause, Object... params) {
		super(cause);
		this.setKey(key);
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
		return getLocalizedMessage();
	}
	
	public String getLocalizedMessage() {
		try {
			return getMessageSource().getMessage(getKey(), params, getLocale());
		} catch (Exception e) {
			log.warn("no message for: "+getKey()+" ("+getLocale()+")");
		}
		return getKey();
	}

	@Override
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	} 
	
	

}
