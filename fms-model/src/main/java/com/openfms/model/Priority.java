package com.openfms.model;

public enum Priority {
	
	LOW(-1,"Low"),
	NORMAL(0,"Normal"),
	HIGH(1,"High"),
	URGENT(2,"Urgent");
	
	private Priority(int value, String status) {
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
