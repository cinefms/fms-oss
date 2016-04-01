package com.openfms.core.service.project.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.api.exceptions.DBStoreException;
import com.openfms.core.service.impl.AbstractFmsDataStore;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectHolder;

@Service
public class ProjectDataStore extends AbstractFmsDataStore {
	
	private static Log log = LogFactory.getLog(ProjectDataStore.class);

	
	public ProjectDataStore() {
		super();
		log.info("#### instantiated!");
	}

	
	protected String getDBName() throws DBStoreException {
		FmsProject p = FmsProjectHolder.get();
		if(p==null) {
			throw new DBStoreException("NO_PROJECT",null);
		}
		return p.getId();
	}
	
	

}
