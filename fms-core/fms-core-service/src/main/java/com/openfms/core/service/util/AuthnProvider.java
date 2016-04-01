package com.openfms.core.service.util;

import com.openfms.model.core.auth.FmsAuthentication;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.exceptions.AuthenticationFailedException;

public interface AuthnProvider {
	
	public boolean supports(Class<? extends FmsAuthentication> auth);
	
	public FmsUser authenticate(FmsAuthentication auth) throws AuthenticationFailedException;

	public void reset(FmsAuthentication auth);
	
}
