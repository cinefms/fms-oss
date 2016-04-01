package com.openfms.model.core.playback;

import com.openfms.model.Status;

public class FmsError {

	private int status = Status.NOT_APPLICABLE.value();
	private String message;
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
	
}
