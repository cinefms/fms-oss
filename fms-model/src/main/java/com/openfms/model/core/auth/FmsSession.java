package com.openfms.model.core.auth;

import com.openfms.utils.common.GuidGenerator;


public class FmsSession {

	private FmsUser user;
	private String userId;
	private String key = GuidGenerator.getToken(28);

	public FmsUser getUser() {
		return user;
	}

	public void setUser(FmsUser user) {
		this.user = user;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName()+":@"+ super.hashCode();
	}

}
