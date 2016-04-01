package com.openfms.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openfms.core.service.AuthnService;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.SessionStore;
import com.openfms.core.service.util.AuthnProvider;
import com.openfms.model.core.auth.FmsAuthentication;
import com.openfms.model.core.auth.FmsSession;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.AuthenticationFailedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.NotAuthenticatedException;
import com.openfms.model.exceptions.NotAuthorizedException;
import com.openfms.utils.common.GuidGenerator;

@Component
public class AuthnServiceImpl implements AuthnService {

	private static Log log = LogFactory.getLog(AuthnServiceImpl.class);

	@Autowired
	private AuthzService authzService;

	@Autowired
	private SessionStore sessionStore;

	@Autowired
	private List<AuthnProvider> providers = new ArrayList<>();

	@Override
	public void logout() {
		FmsSession session = FmsSessionHolder.get();
		destroySession(session);
		FmsSessionHolder.set(createSession());
	}

	@Override
	public FmsUser authenticate(FmsAuthentication auth) throws AccessDeniedException {
		if(log.isDebugEnabled()){ log.debug("incoming session is: " + FmsSessionHolder.get()); }
		try {
			if(log.isDebugEnabled()){ log.debug("trying: "+providers.size()+" providers ... "); }
			for (AuthnProvider ap : providers) {
				if(log.isDebugEnabled()){ log.debug("trying: "+providers.size()+" providers: "+ap.getClass().getCanonicalName()); }
				if (ap.supports(auth.getClass())) {
					if(log.isDebugEnabled()){ log.debug("trying: "+providers.size()+" providers: "+ap.getClass().getCanonicalName()+" supported ... "); }
					try {
						FmsUser user = ap.authenticate(auth);
						if (user == null) {
							if(log.isDebugEnabled()){ log.debug("trying: "+providers.size()+" providers: "+ap.getClass().getCanonicalName()+" supported ... but NULL response"); }
						} else if (!user.isEnabled()) {
							if(log.isDebugEnabled()){ log.debug("trying: "+providers.size()+" providers: "+ap.getClass().getCanonicalName()+" supported ... but user DISABLED"); }
							throw new NotAuthorizedException();
						} else {
							if(log.isDebugEnabled()){ log.debug("trying: "+providers.size()+" providers: "+ap.getClass().getCanonicalName()+" supported ... YEP! replacing existing session ... "); }
							destroySession(FmsSessionHolder.get());
							FmsSessionHolder.clear();
							if(log.isDebugEnabled()){ log.debug("new session is: " + FmsSessionHolder.get()); }
							return user;
						}
					} catch (Exception e) {
						log.error("error authenticating ... ",e);
						if (e instanceof AccessDeniedException) {
							throw (AccessDeniedException) e;
						} else {
							throw new DatabaseException(e);
						}
					}
				}
			}
			throw new NotAuthenticatedException();
		} catch (DatabaseException e) {
			log.error("error authenticating: ", e);
			throw new AuthenticationFailedException();
		} finally {
			if(log.isDebugEnabled()){ log.debug("outgoing session is: " + FmsSessionHolder.get()); }
		}
	}

	@Override
	public void resetAuthentication(FmsAuthentication auth) {
		for(AuthnProvider ap : getProviders()) {
			ap.reset(auth);
		}
	}

	@Override
	public FmsSession createSession() {
		FmsSession session = new FmsSession();
		session.setKey(GuidGenerator.getSessionId());
		updateSession(session);
		if(log.isDebugEnabled()){ log.debug("CREATE: session " + session.getKey()); }
		return session;
	}

	public List<AuthnProvider> getProviders() {
		return providers;
	}

	public void setProviders(List<AuthnProvider> providers) {
		this.providers = providers;
	}

	@Override
	public FmsSession getSession(String key) {
		if(key==null) {
			return null;
		}
		return sessionStore.getSession(key);
	}

	@Override
	public void destroySession(FmsSession session) {
		if(session==null) {
			return;
		}
		sessionStore.destroySession(session);
	}

	@Override
	public void updateSession(FmsSession session) {
		if(session==null || session.getKey()==null) {
			return;
		}
		sessionStore.updateSession(session);
	}

}
