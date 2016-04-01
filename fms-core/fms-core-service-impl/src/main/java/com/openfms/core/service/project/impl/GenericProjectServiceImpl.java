package com.openfms.core.service.project.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.GenericService;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

public abstract class GenericProjectServiceImpl<T extends FmsObject> implements GenericService<T> {

	private static Log log = LogFactory.getLog(GenericProjectServiceImpl.class);
	
	@Autowired
	protected AuthzService authzService;
	
	@Autowired
	protected ProjectDataStore dataStore;
	
	private Class<T> clazz;
	
	@PostConstruct
	public void init() {
		log.info(this.getClass()+" ---> project data store: "+dataStore);
	}

	
	@Override
	public Class<T> getClazz() {
		if(clazz==null) {
			clazz =  (Class<T>)((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return clazz;
	}
	
	
	@Override
	public List<T> list(DBStoreQuery query) throws AccessDeniedException, DatabaseException {
		if(!authzService.allowAccess(getClazz(), AccessType.READ)) {
			throw new AccessDeniedException();
		}
		query = query == null? BasicQuery.createQuery().order("name"):query;
		return dataStore.findObjects(getClazz(),query);
	}

	@Override
	public T get(String id) throws AccessDeniedException, DatabaseException, EntityNotFoundException {
		if(!authzService.allowAccess(getClazz(), id, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		T out = dataStore.getObject(getClazz(), id);
		if(out==null) {
			throw new EntityNotFoundException(getClazz(),id);
		}
		return out;
	}
	
	public boolean update(String id) throws AccessDeniedException, DatabaseException, EntityNotFoundException, VersioningException {
		T a = get(id); 
		T b = dataStore.saveObject(a);
		return a.getVersion()!=b.getVersion();
	}
	
	@Override
	public T save(T t) throws AccessDeniedException, DatabaseException, VersioningException {
		return save(t,false,true);
	}

	@Override
	public T save(T t, boolean override, boolean triggerUpdates) throws AccessDeniedException, DatabaseException, VersioningException {
		if(t.getId()==null && !authzService.allowAccess(t, AccessType.CREATE)) {
			throw new AccessDeniedException();
		} else if(t.getId()!=null && !authzService.allowAccess(t, AccessType.UPDATE)) {
			throw new AccessDeniedException();
		}
		T out = dataStore.saveObject(t,override,triggerUpdates);
		return out;
	}

	@Override
	public void delete(String id) throws AccessDeniedException, DatabaseException {
		if(!authzService.allowAccess(getClazz(), id, AccessType.DELETE)) {
			throw new AccessDeniedException();
		}
		dataStore.deleteObject(getClazz(),id);
	}

}
