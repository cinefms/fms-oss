package com.openfms.core.service.admin.impl.auth;

import org.springframework.stereotype.Component;

import com.openfms.core.service.util.AuthzProvider;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsUser;

@Component
public class AdminCanDoAnythingAuthzProvider implements AuthzProvider {

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public Boolean allowAccess(FmsUser user, Class<? extends FmsObject> type, String id, AccessType access) {
		if (user != null && user.isAdmin()) {
			return true;
		}
		return null;
	}

}
