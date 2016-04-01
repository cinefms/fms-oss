package com.openfms.utils.common.conf.impl;

import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.openfms.utils.common.conf.PropertyLookup;

public class JndiPropertyLookup implements PropertyLookup {

	private static Log log = LogFactory.getLog(JndiPropertyLookup.class);

	private String[] jndiPrefixes = new String [] { "java:comp/env/", "env/", "" } ;

	public String getProperty(String placeholder) {
		InitialContext initialContext = null;
		try {
			List<String> pls = new ArrayList<String>();
			if(placeholder.indexOf(".")>-1) {
				pls.add(placeholder.replaceAll("\\.", "_"));
				pls.add(placeholder.replaceAll("\\.", "/"));
			} else {
				pls.add(placeholder);
			}
			initialContext = new InitialContext();
			for(String pl : pls) {
				for(String jndiPrefix : jndiPrefixes) {
					try {
						log.info("trying to look up in JNDI: "+jndiPrefix + pl);
						return (String) initialContext.lookup(jndiPrefix + pl);
					} catch (NameNotFoundException e) {
						log.warn("error finding a name in JNDI");
					}
				}
			}
		} catch (NamingException e) {
			log.error("error binding JNDI", e);
		} finally {
			if (initialContext != null) {
				try {
					initialContext.close();
				} catch (NamingException e) {
				}
			}
		}
		return null;
	}


}
