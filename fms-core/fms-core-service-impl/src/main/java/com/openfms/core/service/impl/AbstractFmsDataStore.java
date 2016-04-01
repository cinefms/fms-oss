package com.openfms.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.cinefms.dbstore.api.DBStoreBinary;
import com.cinefms.dbstore.api.DBStoreEntity;
import com.cinefms.dbstore.api.DataStore;
import com.cinefms.dbstore.api.exceptions.DBStoreException;
import com.cinefms.dbstore.api.exceptions.EntityNotFoundException;
import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openfms.core.service.impl.listeners.FmsListener;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.VersioningException;
import com.skjlls.aspects.metrics.annotations.Metrics;

public abstract class AbstractFmsDataStore {

	@Autowired
	private ApplicationContext applicationContext;
	
	private static ObjectMapper om = new ObjectMapper();
	
	static {
		om.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
	@Autowired
	private DataStore dataStore;
	
	private static Log log = LogFactory.getLog(AbstractFmsDataStore.class);

	public AbstractFmsDataStore() {
		log.info("#### instantiated!");
	}

	protected abstract String getDBName() throws DBStoreException;
	
	private Map<Class<?>, List<FmsListener>> listeners = new HashMap<Class<?>, List<FmsListener>>();
	
	protected <T extends FmsObject> T clone(T source) {
		if (source == null) {
			return null;
		}
		try {
			@SuppressWarnings("unchecked")
			T out = (T) source.getClass().newInstance();
			PropertyUtils.copyProperties(out, source);
			return out;
		} catch (Exception e) {
			throw new RuntimeException("error cloning into new: ", e);
		}
	}
	
	
	public <T extends FmsObject> List<FmsListener> getListeners(Class<T> clazz) {
		List<FmsListener> out = listeners.get(clazz);
		if(out == null) {
			out = new ArrayList<FmsListener>();
			for(FmsListener l : applicationContext.getBeansOfType(FmsListener.class).values()) {
				if(l.supports(clazz)) {
					out.add(l);
				}
			}
			listeners.put(clazz, out);
		}
		return out;
	}
	
	private <T extends FmsObject> void isVersionOk(T oldObject, T newObject) throws VersioningException {
		if (oldObject == null) {
			// ignore
		} else if (newObject.getVersion()>0 && oldObject.getVersion() > newObject.getVersion()) {
			throw new VersioningException(oldObject.getClass().getCanonicalName(), newObject.getId(), oldObject.getVersion(), newObject.getVersion());
		}
	}		
		
	
	private <T extends FmsObject> boolean isChanged(T oldObject, T newObject) {
		try {
			if(oldObject!=null) {
				
				Date y = oldObject.getUpdated();
				Date x = new Date();
				oldObject.setUpdated(x);
				newObject.setUpdated(x);
				
				newObject.setCreated(oldObject.getCreated());
				
				if(oldObject == newObject) {
					log.warn("old object and new object are the same instance, something is wrong!");
					return true;
				}
				
				String a = om.writeValueAsString(oldObject);
				String b = om.writeValueAsString(newObject);

				if (a.compareTo(b)==0) {
					oldObject.setUpdated(y);
					return false;
				} else {
					return true;
				}
			}

		} catch (Exception e) {
			log.warn("error doing old/new comparison ... ",e);
		}
		return true;
	}
	
	public <T extends FmsObject> boolean wantsSaving(T newObject) throws DBStoreException {
		for(FmsListener fl : getListeners(newObject.getClass())) {
			if(!fl.beforeSave(getDBName(), newObject)) {
				return false;
			}
		}
		return true;
	}
	
	@Metrics(value="save",count=true,time=true,error=false)
	public <T extends FmsObject> T saveObject(T newObject) throws DatabaseException, VersioningException {
		log.debug("save object (normal) "+newObject.getClass());
		return saveObject(newObject, false, true);
	}
	
	
	public void saveDirect(DBStoreEntity x) throws EntityNotFoundException, DBStoreException {
		dataStore.saveObject(getDBName(), x);
	}
	
	@Metrics(value="save",count=true,time=true,error=false)
	@SuppressWarnings("unchecked")
	public <T extends FmsObject> T saveObject(T newObject, boolean override, boolean triggerUpdates) throws DatabaseException, VersioningException {
		try {
			log.debug("save object (override:"+override+", trigger:"+triggerUpdates+") "+newObject.getClass());

			if(!override && !wantsSaving(newObject)) {
				log.info("object does not want saving ... ");
				return clone(newObject);
			}
			
			T oldObject = null;
			if (newObject.getId() != null) {
				oldObject = (T) dataStore.getObject(getDBName(),newObject.getClass(), newObject.getId());
			} 
			
			isVersionOk(oldObject, newObject);
			
			
			if(override) {
				if(log.isDebugEnabled()){ log.debug(" saving object " + newObject.getClass() + " with override, updating ... "); }
			} else if(isChanged(oldObject, newObject)) {
				if(log.isDebugEnabled()){ log.debug(" saving object " + newObject.getClass() + " would change the DB, updating ... "); }
			} else {
				if(log.isDebugEnabled()){ log.debug(" saving object " + newObject.getClass() + " would leave DB unchanged, returning ... "); }
				return clone(oldObject);
			}

			if(oldObject!=null) {
				newObject.setVersion(newObject.getVersion() + 1);
				newObject.setUpdated(new Date());
			} else {
				newObject.setCreated(new Date());
			}
			newObject.setUpdated(new Date());
			newObject = dataStore.saveObject(getDBName(),newObject);

			for(FmsListener fl : getListeners(newObject.getClass())) {
				if(oldObject!=null) {
					fl.updated(getDBName(), oldObject, newObject);
				} else {
					fl.created(getDBName(), newObject);
				}
			}
			
			return (T)getObject(newObject.getClass(), newObject.getId());
		} catch (Exception e) {
			if(e instanceof VersioningException) {
				throw (VersioningException)e;
			}
			log.error("error saving object: ",e);
			throw new DatabaseException(e);
		}
	}

	public <T extends FmsObject> boolean deleteObject(Class<T> clazz, String id) throws DatabaseException {
		T o = getObject(clazz, id);
		return deleteObject(o);
	}

	public <T extends FmsObject> boolean deleteObject(T object) throws DatabaseException {
		try {
			if(object==null) {
				return false;
			}
			for(FmsListener fl : getListeners(object.getClass())) {
				fl.beforeDelete(getDBName(), object);
			}

			boolean out = dataStore.deleteObject(getDBName(),  object);
			
			for(FmsListener fl : getListeners(object.getClass())) {
				fl.deleted(getDBName(), object);
			}
			
			return out;
		} catch (DBStoreException e) {
			throw new DatabaseException(e);
		}
	}

	public <T extends FmsObject> T getObject(Class<T> clazz, String id) throws DatabaseException {
		try {
			return clone(dataStore.getObject(getDBName(),  clazz, id));
		} catch (DBStoreException e) {
			throw new DatabaseException(e);
		}
	}

	public <T extends FmsObject> List<T> findObjects(Class<T> clazz, DBStoreQuery query) throws DatabaseException {
		try {
			List<T> out = new ArrayList<T>();
			if(log.isDebugEnabled()){ log.debug(" ======================================================================= "); }
			if(log.isDebugEnabled()){ log.debug(" == "); }
			if(log.isDebugEnabled()){ log.debug(" == "+clazz+" : "+query.toString()); }
			if(log.isDebugEnabled()){ log.debug(" == "); }
			if(log.isDebugEnabled()){ log.debug(" ======================================================================= "); }
			for (T t : dataStore.findObjects(getDBName(),clazz, query)) {
				out.add(clone(t));
			}
			return out;
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}

	public <T extends FmsObject> T findObject(Class<T> clazz, DBStoreQuery query) throws DatabaseException {
		try {
			return clone(dataStore.findObject(getDBName(),  clazz, query));
		} catch (DBStoreException e) {
			throw new DatabaseException(e);
		}
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}
	
	
	public void saveBinary(DBStoreBinary binary, String bucket) throws DBStoreException {
		dataStore.storeBinary(getDBName(), bucket, binary);
	}
	
	public DBStoreBinary getBinary(String bucket, String id) throws DBStoreException {
		return dataStore.getBinary(getDBName(), bucket, id);
	}
	
	
	public static void main(String[] args) {
		FmsMediaClip mc1 = new FmsMediaClip();
		FmsMediaClip mc2 = new FmsMediaClip();
		mc1.setUpdated(new Date(3));
		mc2.setUpdated(new Date(4));
		System.err.println(mc1.equals(mc2));
	}
	

}
