package com.openfms.model.core.scheduledjobs;

import java.util.Date;

public class FmsScheduledJobLogBrief {

	private String id;
	private int status;
	private String message;
	private Date date;

	public FmsScheduledJobLogBrief() {
	}

	public FmsScheduledJobLogBrief(FmsScheduledJobLog log) {
		this.id = log.getId();
		this.setDate(log.getCreated());
		this.setMessage(log.getMessage());
		this.setStatus(log.getStatus());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
