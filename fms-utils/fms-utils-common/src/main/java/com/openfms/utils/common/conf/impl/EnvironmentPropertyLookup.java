package com.openfms.utils.common.conf.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.openfms.utils.common.conf.PropertyLookup;


public class EnvironmentPropertyLookup implements PropertyLookup {

	private static Log log = LogFactory.getLog(EnvironmentPropertyLookup.class);
	
	public String getProperty(String propertyName) {
		try {
			String[] ts = new String[] { 
					propertyName,
					propertyName.replaceAll("\\.", "_"),
					propertyName.replaceAll("\\.", "-")
			};

			String s = null;
			for(String t : ts) {
				s = System.getenv(t);
				if(log.isDebugEnabled()){ log.debug("trying: "+t+" ---> "+s); }
				if(s!=null) {
					break;
				}
			}
			return s;
		} catch (Exception e) {
			log.error("error looking up environment: ",e);
			return null;
		}
	}

}
