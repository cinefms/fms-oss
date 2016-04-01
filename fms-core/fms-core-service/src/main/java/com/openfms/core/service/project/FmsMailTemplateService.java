package com.openfms.core.service.project;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.openfms.model.core.project.FmsMailTemplate;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

public interface FmsMailTemplateService {
	
	public List<FmsMailTemplate> list(DBStoreQuery query) throws AccessDeniedException, DatabaseException;
	
	public FmsMailTemplate get(String templateId) throws AccessDeniedException, DatabaseException, EntityNotFoundException;
	
	public FmsMailTemplate getByName(String name) throws AccessDeniedException, DatabaseException, EntityNotFoundException;
	
	public FmsMailTemplate save(FmsMailTemplate template) throws AccessDeniedException, DatabaseException, VersioningException, EntityNotFoundException;
	
	public void delete(String templateId) throws AccessDeniedException, DatabaseException;

	public void sendTest(String id, String recipient, Map<String, Object> params) throws AccessDeniedException, DatabaseException, EntityNotFoundException, IOException;
	

}
