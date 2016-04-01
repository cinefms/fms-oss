package com.openfms.utils.common.conf.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.openfms.utils.common.conf.PropertyLookup;

public class ResourcePropertyLookup implements PropertyLookup {

	private Log log = LogFactory.getLog(ResourcePropertyLookup.class);
	
	private List<Properties> properties = new ArrayList<Properties>();
	
	public String getProperty(String propertyName) {
		for(Properties p : properties) {
			String s = p.getProperty(propertyName);
			if(s!=null) {
				return s;
			}
		}
		return null;
	}

	public void setResources(String resources) throws IOException {
		Resource[] rs = new PathMatchingResourcePatternResolver().getResources(resources);
		log.info("loading properties from "+rs.length+" files ... ");
		for(Resource r : rs) {
			Properties p = new Properties();
			log.info("loading properties from "+r.getURI()+" ... ");
			p.load(r.getInputStream());
			log.info("loading properties from "+r.getURI()+" ... OK!");
			properties.add(p);
		}
	}
	
}
