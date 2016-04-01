package com.openfms.core.service.admin.impl.auth;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.admin.impl.AdminDataStore;
import com.openfms.core.service.util.AuthnProvider;
import com.openfms.model.core.auth.FmsAuthentication;
import com.openfms.model.core.auth.FmsPassword;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.exceptions.AuthenticationFailedException;
import com.openfms.utils.common.GuidGenerator;

@Component
public class AdminUserAuthnProvider implements AuthnProvider {

	private static Log log = LogFactory.getLog(AdminUserAuthnProvider.class);
	
	@Autowired
	private AdminDataStore dataStore;

	@PostConstruct
	public void init() {
		try {
			log.info(" ----> "+this.hashCode());
			log.info(this.getClass()+" ---> admin data store: "+dataStore);
			FmsAdminUser fau = dataStore.findObject(FmsAdminUser.class, BasicQuery.createQuery().eq("emailUpper", "ADMIN@OPENFMS.COM"));
			if(fau==null) {
				fau = new FmsAdminUser();
				fau.setDisplayName("Admin");
				fau.setEmail("admin@openfms.com");
				fau.setName("Alfred E. Admin");
				fau.setEnabled(true);
				fau = dataStore.saveObject(fau);
				
				FmsPassword ep = new FmsPassword();
				ep.setUserId(fau.getId());
				ep.setPassword("admin123");
				dataStore.saveObject(ep);
			}
			
		} catch (Exception e) {
			log.error("error checking or setting up admin user!",e);
		}
	}
	
	@Override
	public boolean supports(Class<? extends FmsAuthentication> auth) {
		return true;
	}

	@Override
	public FmsUser authenticate(FmsAuthentication auth) throws AuthenticationFailedException {
		if(auth.getEmailAddress()==null) {
			return null;
		}
		try {
			log.info(" ----> "+this.hashCode());
			log.info(" ----> "+auth);
			log.info(" ----> "+auth.getEmailAddress());
			log.info(" ----> "+auth.getEmailAddress().toUpperCase());
			log.info(" ----> "+dataStore);
			FmsAdminUser user = dataStore.findObject(FmsAdminUser.class, BasicQuery.createQuery().eq("emailUpper", auth.getEmailAddress().toUpperCase()));
			if (user != null) {
				FmsPassword fup = dataStore.findObject(FmsPassword.class, BasicQuery.createQuery().eq("userId", user.getId()));
				if (fup != null) {
					if (fup.compare(auth.getPassword())) {
						fup.setNewCryptedPassword(null);
						dataStore.saveObject(fup);
						return user;
					}
					if (fup.compareNew(auth.getPassword())) {
						fup.setPassword(auth.getPassword());
						dataStore.saveObject(fup);
						return user;
					}
				}
			}
			return null;
		} catch (Exception e) {
			throw new AuthenticationFailedException("AUTH_FAILED", e);
		}
	}

	@Override
	public void reset(FmsAuthentication auth) {
		try {
			FmsAdminUser user = dataStore.findObject(FmsAdminUser.class, BasicQuery.createQuery().eq("emailUpper", auth.getEmailAddress().toUpperCase()));
			if (user != null) {
				FmsPassword fup = dataStore.findObject(FmsPassword.class, BasicQuery.createQuery().eq("userId", user.getId()));
				if (fup == null) {
					fup = new FmsPassword();
					fup.setUserId(user.getId());
				}
				String x = GuidGenerator.getPassword(12);
				fup.setNewCryptedPassword(x);
				dataStore.saveObject(fup);
			}
		} catch (Exception e) {
			log.error("error resetting password",e);
		}
	}
	
}
