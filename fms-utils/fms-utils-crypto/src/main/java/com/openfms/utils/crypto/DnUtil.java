package com.openfms.utils.crypto;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

public class DnUtil {


	public static boolean compareDn(String dnOne, String dnTwo) {
		return false;
	}
	
	public static String getDnComponent(String dn, String component) {
		try {
			LdapName ln = new LdapName(dn);
			
			for (Rdn r : ln.getRdns()) {
				if(r.getType().compareToIgnoreCase(component)==0) {
					return r.getValue()+"";
				}
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException("error parsing DN: ",e);
			
		}
	}

	
	
}
