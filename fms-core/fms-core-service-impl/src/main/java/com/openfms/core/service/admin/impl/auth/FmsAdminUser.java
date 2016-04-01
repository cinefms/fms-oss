package com.openfms.core.service.admin.impl.auth;

import com.openfms.model.core.auth.FmsUser;

public class FmsAdminUser extends FmsUser {

	private static final long serialVersionUID = 9014426533506320943L;
	
	public FmsAdminUser() {
		super();
	}

	@Override
	public boolean isAdmin() {
		return true;
	}

}
