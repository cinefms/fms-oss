package com.openfms.model.core.scheduledjobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.openfms.model.Status;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.SCHEDULED_JOBS)
public class FmsScheduledJobConfig extends AbstractFmsObject {

	private static final long serialVersionUID = -4665781919743528187L;
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	private boolean active;
	private String type;
	private String schedule;
	private int status = Status.NOT_APPLICABLE.value();
	private Date lastExecuted;
	
	private List<String> userIds = new ArrayList<String>();
	private List<FmsScheduledJobConfigUser> users = new ArrayList<FmsScheduledJobConfigUser>();
	

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public List<FmsScheduledJobConfigUser> getUsers() {
		return users;
	}

	public void setUsers(List<FmsScheduledJobConfigUser> users) {
		this.users = users;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getLastExecuted() {
		return lastExecuted;
	}

	public void setLastExecuted(Date lastExecuted) {
		this.lastExecuted = lastExecuted;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
