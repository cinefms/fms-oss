package com.openfms.core.service.project.auth.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openfms.core.service.project.FmsProjectGroupService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.core.service.util.AuthzProvider;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.global.FmsCountry;
import com.openfms.model.core.global.FmsLanguage;
import com.openfms.model.core.global.FmsNotification;
import com.openfms.model.core.global.FmsTag;
import com.openfms.model.core.movie.FmsAudioFormat;
import com.openfms.model.core.movie.FmsFramerate;
import com.openfms.model.core.movie.FmsMediaAspect;
import com.openfms.model.core.movie.FmsScreenAspect;

@Component
public class LoggedInUserBasicAuthzProvider implements AuthzProvider {

	private static Log log = LogFactory.getLog(LoggedInUserBasicAuthzProvider.class);

	@Autowired
	private FmsProjectGroupService groupService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	@Override
	public boolean supports(Class<?> clazz) {
		if(clazz == FmsLanguage.class) {
			return true;
		} else if(clazz == FmsCountry.class) {
			return true;
		} else if(clazz == FmsTag.class) {
			return true;
		} else if(clazz == FmsFramerate.class) {
			return true;
		} else if(clazz == FmsMediaAspect.class) {
			return true;
		} else if(clazz == FmsScreenAspect.class) {
			return true;
		} else if(clazz == FmsAudioFormat.class) {
			return true;
		} else if(clazz == FmsNotification.class) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean allowAccess(FmsUser user, Class<? extends FmsObject> type, String id, AccessType access) {
		if(user!=null) {
			return true;
		}
		return null;
	}

}
