package com.openfms.utils.common.conf.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.openfms.utils.common.conf.PropertyLookup;

public class SystemPropertyLookup implements PropertyLookup {

	private static Log log = LogFactory.getLog(SystemPropertyLookup.class);
	
	public SystemPropertyLookup() {
		log.info(" ########################################################### ");
		log.info(" ## ");
		log.info(" ## SYSTEM ENVIRONMENT");
		log.info(" ## ");
		for(Object o : System.getProperties().keySet()) {
			log.info(" ## "+o+" = "+System.getProperty((String)o));
		}
		log.info(" ## ");
		log.info(" ## ");
		log.info(" ########################################################### ");
	}
	
	
	public String getProperty(String propertyName) {
		return System.getProperty(propertyName);
	}

}
