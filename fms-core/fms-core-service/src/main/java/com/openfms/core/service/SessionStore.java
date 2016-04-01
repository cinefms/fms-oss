package com.openfms.core.service;

import com.openfms.model.core.auth.FmsSession;

public interface SessionStore {

	public FmsSession getSession(String key);

	public void updateSession(FmsSession session);

	public void destroySession(FmsSession session);

	public void cleanup();

}
