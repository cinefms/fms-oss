package com.openfms.core.service.project;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.auth.FmsAuthentication;
import com.openfms.model.core.auth.FmsProjectUser;

public interface FmsProjectUserService extends GenericService<FmsProjectUser> {

	public void reset(FmsAuthentication auth);

	
	
}
