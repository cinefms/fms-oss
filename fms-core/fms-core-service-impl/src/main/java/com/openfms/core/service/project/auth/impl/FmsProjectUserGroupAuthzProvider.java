package com.openfms.core.service.project.auth.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsProjectGroupService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.core.service.util.AuthzProvider;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsEntityGroupAcl;
import com.openfms.model.core.auth.FmsGroup;
import com.openfms.model.core.auth.FmsProjectUser;
import com.openfms.model.core.auth.FmsUser;

@Component
public class FmsProjectUserGroupAuthzProvider implements AuthzProvider {

	private static Log log = LogFactory.getLog(FmsProjectUserGroupAuthzProvider.class);

	@Autowired
	private FmsProjectGroupService groupService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	@Override
	public boolean supports(Class<?> clazz) {
		boolean out = clazz.getAnnotation(FmsAccessControlled.class)!=null;
		if(log.isDebugEnabled()){ log.debug(clazz.getCanonicalName()+" - supported?! "+out); }
		return out;
	}

	@Override
	public Boolean allowAccess(FmsUser user, Class<? extends FmsObject> type, String id, AccessType access) {
		try {
			if(user instanceof FmsProjectUser) {
				if(log.isDebugEnabled()){ log.debug("a project user!"); }
				FmsProjectUser pu = (FmsProjectUser)user;
				
				List<FmsGroup> groups = new ArrayList<FmsGroup>();
				try {
					groups = dataStore.findObjects(FmsGroup.class, BasicQuery.createQuery().in("_id", pu.getGroups()));
				} catch (Exception e) {
					log.warn("error getting groups ... ",e);
				}
				FmsAccessControlled a = type.getAnnotation(FmsAccessControlled.class);
				if(log.isDebugEnabled()){ log.debug(groups.size()+" groups!"); }
				if(a!=null) {
					FmsAccessControlled.GROUP g = a.value();
					if(log.isDebugEnabled()){ log.debug(type.getCanonicalName()+": is in entity group "+g); }
					for(FmsGroup group : groups) {
						if(log.isDebugEnabled()){ log.debug(type.getCanonicalName()+": group "+group.getName()+" has "+group.getAcls().size()+" ACLs ... "); }
						for(FmsEntityGroupAcl acl : group.getAcls()) {
							if(log.isDebugEnabled()){ log.debug(type.getCanonicalName()+": acl "+acl.getGroup()+" / "+g+" ... "); }
							if(acl.getGroup() == g) {
								if(log.isDebugEnabled()){ log.debug(type.getCanonicalName()+": acl "+acl.getGroup()+" / has "+acl.getAccess()+" access entries ... "); }
								for(AccessType at : acl.getAccess()) {
									if(log.isDebugEnabled()){ log.debug(type.getCanonicalName()+": acl "+acl.getGroup()+" / has "+acl.getAccess()+" access entries: "+access+" / "+at); }
									if(at == access) {
										if(log.isDebugEnabled()){ log.debug(type.getCanonicalName()+": acl "+acl.getGroup()+" / has "+acl.getAccess()+" access entries: "+access+" / "+at+" - YES"); }
										return true;
									} else {
										if(log.isDebugEnabled()){ log.debug(type.getCanonicalName()+": acl "+acl.getGroup()+" / has "+acl.getAccess()+" access entries: "+access+" / "+at+" - NO"); }
									}
								}
							}
						}
					}
				}
			} else {
				if(log.isDebugEnabled()){ log.debug("not a project user!"); }
			}
		} catch (Exception e) {
			log.warn("error checking access",e);
		}
		return null;
	}

}
