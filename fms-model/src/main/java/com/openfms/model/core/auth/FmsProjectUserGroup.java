package com.openfms.model.core.auth;

import com.openfms.model.core.AbstractFmsObject;

public class FmsProjectUserGroup extends AbstractFmsObject {

	private static final long serialVersionUID = 2572564579190704483L;
	private String userId;
	private String groupId;

	@Override
	public String getId() {
		return userId+"-"+groupId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
