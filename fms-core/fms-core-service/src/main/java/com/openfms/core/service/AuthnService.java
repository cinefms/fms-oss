package com.openfms.core.service;

import com.openfms.model.core.auth.FmsAuthentication;
import com.openfms.model.core.auth.FmsSession;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.exceptions.AccessDeniedException;


public interface AuthnService {

	public void logout();

	public FmsUser authenticate(FmsAuthentication auth) throws AccessDeniedException;

	public void resetAuthentication(FmsAuthentication auth);

	public FmsSession getSession(String s);

	public void destroySession(FmsSession session);

	public FmsSession createSession();

	public void updateSession(FmsSession session);

}
