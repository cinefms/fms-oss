package com.openfms.model.core.movie;

import java.util.Date;
import java.util.List;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.TASKS)
public class FmsMediaClipTaskProgress extends AbstractFmsObject {
	
	private static final long serialVersionUID = 5468836604937310395L;

	private String mediaClipTaskId;
	private String deviceId;
	private Date date;
	private Date dueDate;

	private String user;
	private String userId;

	private String assignTo;
	private String assignToUserName;

	private String comment;
	private Boolean close;
	private Boolean disable;
	private Integer status;
	private Integer priority;

	private List<FmsMediaClipTaskProgressFollowUp> followUps;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getClose() {
		return close;
	}

	public void setClose(Boolean close) {
		this.close = close;
	}

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public String getMediaClipTaskId() {
		return mediaClipTaskId;
	}

	public void setMediaClipTaskId(String mediaClipTaskId) {
		this.mediaClipTaskId = mediaClipTaskId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}

	public String getAssignToUserName() {
		return assignToUserName;
	}

	public void setAssignToUserName(String assignToUserName) {
		this.assignToUserName = assignToUserName;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public List<FmsMediaClipTaskProgressFollowUp> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(List<FmsMediaClipTaskProgressFollowUp> followUps) {
		this.followUps = followUps;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	
}
