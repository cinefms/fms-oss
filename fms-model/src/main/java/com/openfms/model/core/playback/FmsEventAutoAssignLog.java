package com.openfms.model.core.playback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FmsEventAutoAssignLog {

	private Date eventDate;
	private String eventId;
	private String eventName;
	private boolean success = false;
	private String message;
	private List<FmsEventAutoAssignLogItem> items = new ArrayList<>();

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public List<FmsEventAutoAssignLogItem> getItems() {
		return items;
	}

	public void setItems(List<FmsEventAutoAssignLogItem> items) {
		this.items = items;
	}

	public int getTotal() {
		return items.size();
	}

	public void setTotal(int total) {
	}

	public int getModified() {
		int out = 0;
		for (FmsEventAutoAssignLogItem li : items) {
			if (li.isModified()) {
				out++;
			}
		}
		return out;
	}

	public void setModified(int modified) {
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
