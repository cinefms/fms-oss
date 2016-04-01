package com.openfms.model.core.scheduledjobs;

import com.openfms.model.core.auth.FmsUser;

public class FmsScheduledJobConfigUser {

	private String userId;
	private String userName;

	public FmsScheduledJobConfigUser() {
	}
	
	public FmsScheduledJobConfigUser(FmsUser user) {
		this.setUserId(user.getId());
		this.setUserName(user.getName());
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
