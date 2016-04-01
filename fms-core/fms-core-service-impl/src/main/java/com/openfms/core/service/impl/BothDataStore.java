package com.openfms.core.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.api.exceptions.DBStoreException;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectHolder;

@Component
public class BothDataStore extends AbstractFmsDataStore {

	private static Log log = LogFactory.getLog(BothDataStore.class);

	
	public BothDataStore() {
		super();
		log.info("#### instantiated!");
	}
	
	protected String getDBName() throws DBStoreException {
		FmsProject p = FmsProjectHolder.get();
		if(p==null) {
			return "admin";
		}
		return p.getId();

	}
	
	

}
