package com.openfms.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openfms.core.service.AuthzService;
import com.openfms.core.service.util.AuthzProvider;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.NotAuthenticatedException;
import com.skjlls.aspects.metrics.annotations.Metrics;

@Service
public class AuthzServiceImpl implements AuthzService {
	
	private static Log log = LogFactory.getLog(AuthnServiceImpl.class);

	@Autowired
	private List<AuthzProvider> providers = new ArrayList<>();

	@Override
	public boolean allowAccess(FmsObject entity, AccessType access) throws AccessDeniedException {
		if (entity != null) {
			return allowAccess(entity.getClass(), entity.getId(), access);
		}
		return false;
	}

	@Override
	public boolean allowAccess(Class<? extends FmsObject> type, AccessType access) throws AccessDeniedException {
		if (type != null) {
			return allowAccess(type, null, access);
		}
		return false;
	}

	@Metrics("authz")
	@Override
	public boolean allowAccess(Class<? extends FmsObject> type, String id, AccessType access) throws AccessDeniedException {
		if (type == null) {
			return false;
		} else {
			FmsUser user = FmsSessionHolder.getCurrentUser();
			if(user!=null && user.isAdmin()) {
				return true;
			}
			for (AuthzProvider p : providers) {
				if(log.isDebugEnabled()){ log.debug("authz service: checking - "+p.getClass()); }
				if(user==null) {
					throw new NotAuthenticatedException();
				}
				if (p.supports(type)) {
					if(log.isDebugEnabled()){ log.debug("authz service: checking - "+p.getClass()+" ... supported"); }
					Boolean b = p.allowAccess(user, type, id, access);
					if (b != null) {
						if(log.isDebugEnabled()){ log.debug("authz service: checking - "+p.getClass()+" ... supported ... success!"); }
						return b.booleanValue();
					}
				}

			}
		}
		return false;
	}

	public List<AuthzProvider> getProviders() {
		return providers;
	}

	public void setProviders(List<AuthzProvider> providers) {
		this.providers = providers;
	}

}
