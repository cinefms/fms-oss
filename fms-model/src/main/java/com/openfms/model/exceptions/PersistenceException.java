package com.openfms.model.exceptions;

public class PersistenceException extends BaseException {

	private static final long serialVersionUID = -2046574207382041104L;
	private Class<?> type;
	private String id;

	public PersistenceException(Class<?> type, String id) {
		this(null,type,id);
	}

	public PersistenceException(Throwable t, Class<?> type, String id) {
		super(t);
		this.id = id;
		this.type = type;
	}
	
	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
