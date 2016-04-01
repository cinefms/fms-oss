package com.openfms.core.service.project.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.project.FmsMailService;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.core.service.project.FmsProjectUserService;
import com.openfms.core.service.util.AuthnProvider;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsAuthentication;
import com.openfms.model.core.auth.FmsPassword;
import com.openfms.model.core.auth.FmsProjectUser;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.core.project.FmsProjectHolder;
import com.openfms.model.exceptions.AuthenticationFailedException;
import com.openfms.utils.common.GuidGenerator;

@Service
public class FmsProjectUserServiceImpl extends GenericProjectServiceImpl<FmsProjectUser> implements FmsProjectUserService, AuthnProvider {

	private static Log log = LogFactory.getLog(FmsProjectUserServiceImpl.class);
	
	@Autowired
	private AuthzService authzService;
	
	@Autowired
	private FmsMailService mailService;

	@Autowired
	private FmsMovieService movieService;

	@Override
	public boolean supports(Class<? extends FmsAuthentication> auth) {
		return true;
	}

	@Override
	public FmsUser authenticate(FmsAuthentication auth) throws AuthenticationFailedException {
		if(FmsProjectHolder.get()==null) {
			// no project = no project user
			return null;
		}
		if(auth.getUsername()==null) {
			// no username = not a project user
			return null;
		}
		try {
			log.info(" ----> "+auth);
			log.info(" ----> "+auth.getUsername());
			log.info(" ----> "+auth.getUsername().toUpperCase());
			log.info(" ----> "+dataStore);
			FmsProjectUser user = dataStore.findObject(FmsProjectUser.class, BasicQuery.createQuery().eq("usernameUpper", auth.getUsername().toUpperCase()));
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
			if(auth.getUsername()!=null) {
				if(auth.getPassword()==null && auth.getNewPassword()==null) {
					resetPassword(auth);
					return;
				}

				FmsProjectUser user = dataStore.findObject(FmsProjectUser.class, BasicQuery.createQuery().eq("usernameUpper", auth.getUsername().toUpperCase()));
				if(user==null) {
					return;
				}

				
				if(authzService.allowAccess(FmsProjectUser.class, AccessType.ADMIN)) {
					// all good
				} else if(auth.getPassword()==null) {
					// return
				} 
				
				FmsPassword fup = dataStore.findObject(FmsPassword.class, BasicQuery.createQuery().eq("userId", user.getId()));
				if(fup==null) {
					resetPassword(auth);
				} else if(authzService.allowAccess(FmsProjectUser.class, AccessType.ADMIN) || fup.compare(auth.getPassword())) {
					fup.setNewPassword(auth.getNewPassword());
					dataStore.saveObject(fup);
				}
		
			}

		} catch (Exception e) {
			log.error("error resetting password",e);
		}
	}

	public void resetPassword(FmsAuthentication auth) {
		try {
			FmsProjectUser user = dataStore.findObject(FmsProjectUser.class, BasicQuery.createQuery().eq("usernameUpper", auth.getUsername().toUpperCase()));
			if(user==null) {
				return;
			}
			Map<String,Object> tokens = new HashMap<String, Object>();
			tokens.put("user", user);
			
			FmsPassword fup = dataStore.findObject(FmsPassword.class, BasicQuery.createQuery().eq("userId", user.getId()));
			if(fup==null) {
				fup = new FmsPassword();
				fup.setUserId(user.getId());
			}
			String p = GuidGenerator.getPassword(12);
			tokens.put("password", p);
			fup.setNewPassword(p);
			dataStore.saveObject(fup);
			
			List<FmsMovie> movies = new ArrayList<FmsMovie>();
			
			if(user.getMovieIds()!=null && user.getMovieIds().size()>0) {
				for(String movieId : user.getMovieIds()) {
					try {
						FmsMovie m = movieService.get(movieId);
						movies.add(m);
					} catch (Exception e) {
						log.warn("user has a movie configured that doesn't exist!");
					}
				}
				tokens.put("movies", movies);
			}
			
			
			mailService.sendMail(auth.getType(), tokens, user.getEmail());
			
		} catch (Exception e) {
			log.error("error resetting password",e);
		}
	}
	
}
