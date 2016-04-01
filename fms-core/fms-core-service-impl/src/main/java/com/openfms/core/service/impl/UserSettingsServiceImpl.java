package com.openfms.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.UserSettingsService;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.auth.FmsUserSettings;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.NotAuthenticatedException;
import com.openfms.model.exceptions.VersioningException;

@Component
public class UserSettingsServiceImpl implements UserSettingsService {

	@Autowired
	private BothDataStore dataStore;
	
	@Override
	public FmsUserSettings getSettings() throws AccessDeniedException, DatabaseException, VersioningException  {
		FmsUser u = FmsSessionHolder.getCurrentUser();
		if(u!=null) {
			FmsUserSettings out = dataStore.findObject(FmsUserSettings.class, BasicQuery.createQuery().eq("userId", u.getId()));
			if(out==null) {
				out = new FmsUserSettings();
				saveSettings(out);
			}
			return out;
		}
		throw new NotAuthenticatedException();
	}

	@Override
	public FmsUserSettings saveSettings(FmsUserSettings settings) throws AccessDeniedException, DatabaseException, VersioningException {
		FmsUser u = FmsSessionHolder.getCurrentUser();
		if(u!=null) {
			FmsUserSettings out = dataStore.findObject(FmsUserSettings.class, BasicQuery.createQuery().eq("userId", u.getId()));
			if(out!=null) {
				settings.setId(out.getId());
			}
			settings.setUserId(u.getId());
			return dataStore.saveObject(settings);
		}
		throw new NotAuthenticatedException();
	}

}
