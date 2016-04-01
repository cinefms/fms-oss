package com.openfms.core.service.admin.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.api.exceptions.DBStoreException;
import com.openfms.core.service.impl.AbstractFmsDataStore;

@Component
public class AdminDataStore extends AbstractFmsDataStore {

	private static Log log = LogFactory.getLog(AdminDataStore.class);
	
	public AdminDataStore() {
		log.info("#### instantiated!");
	}
	
	@Override
	protected String getDBName() throws DBStoreException {
		return "admin";
	}

}
