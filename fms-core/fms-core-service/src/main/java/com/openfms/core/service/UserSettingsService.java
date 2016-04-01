package com.openfms.core.service;

import com.openfms.model.core.auth.FmsUserSettings;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.VersioningException;

public interface UserSettingsService {
	
	public FmsUserSettings getSettings() throws AccessDeniedException, DatabaseException, VersioningException;

	public FmsUserSettings saveSettings(FmsUserSettings settings) throws AccessDeniedException, DatabaseException, VersioningException;

}
