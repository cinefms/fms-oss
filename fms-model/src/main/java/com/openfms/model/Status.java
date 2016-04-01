package com.openfms.model;

public enum Status {
	
	ERROR(0,"Error"),
	WARNING(1,"Warning"),
	PENDING(2,"Pending"),
	OK(3,"Ok"),
	NOT_APPLICABLE(-1,"Not applicable");
	
	private Status(int value, String status) {
		this.value = value;
		this.status = status;
	}

	private final int value;
	private final String status;
	
	
	public String status() {
		return status;
	}
	
	public int value() {
		return value;
	}

}
