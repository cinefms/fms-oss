package com.openfms.core.service;

import java.util.List;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.openfms.model.core.FmsObject;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

public interface GenericService<T extends FmsObject> {

	public Class<T> getClazz();
	
	public List<T> list(DBStoreQuery query) throws AccessDeniedException, DatabaseException;
	public T get(String id) throws AccessDeniedException, DatabaseException, EntityNotFoundException;
	public boolean update(String id) throws AccessDeniedException, DatabaseException, EntityNotFoundException, VersioningException;
	public T save(T t) throws AccessDeniedException, DatabaseException, VersioningException;
	public T save(T t, boolean override, boolean triggerUpdates) throws AccessDeniedException, DatabaseException, VersioningException;
	public void delete(String id) throws AccessDeniedException, DatabaseException;

	
}
