package com.openfms.core.service.util;

import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsUser;

public interface AuthzProvider {
	
	public boolean supports(Class<?> clazz);
	
	public Boolean allowAccess(FmsUser user, Class<? extends FmsObject> type, String id, AccessType access);

}
