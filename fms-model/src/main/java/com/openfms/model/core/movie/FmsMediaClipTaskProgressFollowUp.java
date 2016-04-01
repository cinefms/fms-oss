package com.openfms.model.core.movie;

import com.openfms.model.Priority;
import com.openfms.model.Status;

public class FmsMediaClipTaskProgressFollowUp {

	private String created;
	private String type;
	private String comment;
	private boolean create = false;
	private int status = Status.PENDING.value();
	private int priority = Priority.NORMAL.value();
	private String deviceId;
	private String deviceName;

	public FmsMediaClipTaskProgressFollowUp() {
	}
	
	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

}
