package com.openfms.model.core.movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.Priority;
import com.openfms.model.Status;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;
import com.openfms.model.core.references.FmsEventReference;

@Indexes(
		{
			@Index(fields={"mediaClipId"},name="idx_mediaClipId",unique=false),
		}
	)
@FmsAccessControlled(FmsAccessControlled.GROUP.TASKS)
public class FmsMediaClipTask extends AbstractFmsObject {

	private static final long serialVersionUID = 6582529176076115141L;

	private String previousTaskId;
	private String previousTaskType;
	private List<String> followUpTaskIds = new ArrayList<String>();
	
	private String mediaClipId;
	private String mediaClipName;
	private String mediaClipType;
	private boolean mediaClipEncryptionStatus;
	private int mediaClipStatus;
	private String comment;
	private String type;
	private int status = Status.PENDING.value();
	private int priority = Priority.NORMAL.value();
	private boolean closed = false;
	private boolean disabled = false;

	private String userId;
	private String userName;
	private Date lastUpdated;
	private Date nextEventDate;
	private String nextEventId;
	private Date firstEventDate;
	private Date dueDate;

	
	private String deviceId;
	private String deviceName;
	
	private List<FmsEventReference> events = new ArrayList<FmsEventReference>();
	

	public FmsMediaClipTask() {
		super(null);
	}

	public int getStatus() {
		if(userId!=null) {
			return Status.PENDING.value();
		}
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMediaClipId() {
		return mediaClipId;
	}

	public void setMediaClipId(String mediaClipId) {
		this.mediaClipId = mediaClipId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public List<FmsEventReference> getEvents() {
		if(events == null) {
			events = new ArrayList<FmsEventReference>();
		}
		return events;
	}

	public void setEvents(List<FmsEventReference> events) {
		this.events.clear();
		if(events!=null) {
			this.events.addAll(events);
		}
	}

	public String getMediaClipType() {
		return mediaClipType;
	}

	public void setMediaClipType(String mediaClipType) {
		this.mediaClipType = mediaClipType;
	}

	public String getMediaClipName() {
		return mediaClipName;
	}

	public void setMediaClipName(String mediaClipName) {
		this.mediaClipName = mediaClipName;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getMediaClipStatus() {
		return mediaClipStatus;
	}

	public void setMediaClipStatus(int mediaClipStatus) {
		this.mediaClipStatus = mediaClipStatus;
	}

	public boolean isMediaClipEncryptionStatus() {
		return mediaClipEncryptionStatus;
	}

	public void setMediaClipEncryptionStatus(boolean mediaClipEncryptionStatus) {
		this.mediaClipEncryptionStatus = mediaClipEncryptionStatus;
	}

	public String getPreviousTaskId() {
		return previousTaskId;
	}

	public void setPreviousTaskId(String previousTaskId) {
		this.previousTaskId = previousTaskId;
	}

	public List<String> getFollowUpTaskIds() {
		return followUpTaskIds;
	}

	public void setFollowUpTaskIds(List<String> followUpTaskIds) {
		this.followUpTaskIds = followUpTaskIds;
	}

	public String getPreviousTaskType() {
		return previousTaskType;
	}

	public void setPreviousTaskType(String previousTaskType) {
		this.previousTaskType = previousTaskType;
	}

	public Date getFirstEventDate() {
		return firstEventDate;
	}

	public void setFirstEventDate(Date firstEventDate) {
		this.firstEventDate = firstEventDate;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
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
	
	@Override
	public String getSearchable() {
		return super.getSearchable() + (getDeviceName()!=null?getDeviceName():"");
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getNextEventDate() {
		return nextEventDate;
	}

	public void setNextEventDate(Date nextEventDate) {
		this.nextEventDate = nextEventDate;
	}

	public String getNextEventId() {
		return nextEventId;
	}

	public void setNextEventId(String nextEventId) {
		this.nextEventId = nextEventId;
	}
	
	
	
}
