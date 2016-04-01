package com.openfms.core.service.project;

import com.openfms.model.core.config.FmsCredentials;
import com.openfms.model.core.project.FmsMailServerConfig;
import com.openfms.model.core.project.FmsProjectSettings;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

public interface ProjectSettingService {

	public FmsProjectSettings getSettings() throws AccessDeniedException, DatabaseException, VersioningException;
	
	public FmsProjectSettings saveSettings(FmsProjectSettings settings) throws DatabaseException, VersioningException, AccessDeniedException;
	
	public FmsMailServerConfig getMailServerConfig() throws DatabaseException, AccessDeniedException, VersioningException;

	public FmsMailServerConfig saveMailServerConfig(FmsMailServerConfig config) throws DatabaseException, VersioningException, AccessDeniedException;
	
	public void saveCredentials(FmsCredentials c) throws AccessDeniedException, DatabaseException, VersioningException, EntityNotFoundException;
	
}
