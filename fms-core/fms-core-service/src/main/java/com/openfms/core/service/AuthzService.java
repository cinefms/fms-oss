package com.openfms.core.service;

import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.exceptions.AccessDeniedException;

public interface AuthzService {

	public abstract boolean allowAccess(Class<? extends FmsObject> type, String id, AccessType access) throws AccessDeniedException;

	public abstract boolean allowAccess(Class<? extends FmsObject> type, AccessType access) throws AccessDeniedException;

	public abstract boolean allowAccess(FmsObject entity, AccessType access) throws AccessDeniedException;

	
}
