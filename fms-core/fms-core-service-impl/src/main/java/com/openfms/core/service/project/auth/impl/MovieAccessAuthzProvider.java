package com.openfms.core.service.project.auth.impl;

import org.springframework.stereotype.Component;

import com.openfms.core.service.util.AuthzProvider;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsProjectUser;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.crypto.FmsKeyRequest;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.core.movie.FmsMovieVersion;

@Component
public class MovieAccessAuthzProvider implements AuthzProvider {

	@Override
	public boolean supports(Class<?> clazz) {
		if(clazz == FmsKeyRequest.class) {
			return true;
		} else if(clazz == FmsKey.class) {
			return true;
		} else if(clazz == FmsMovie.class) {
			return true;
		} else if(clazz == FmsMediaClip.class) {
			return true;
		} else if(clazz == FmsMovieVersion.class) {
			return true;
		} else if(clazz == FmsKey.class) {
			return true;
		} else if(clazz == FmsKeyRequest.class) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean allowAccess(FmsUser user, Class<? extends FmsObject> type, String id, AccessType access) {
		if(user !=null && user instanceof FmsProjectUser) {
			FmsProjectUser fpu = (FmsProjectUser)user;
			if(fpu.getMovieIds()!=null && fpu.getMovieIds().size()>0) {
				if(type == FmsKey.class) {
					if(access == AccessType.CREATE || access == AccessType.READ) {
						return true;
					} else {
						return false;
					}
				}
				if(type == FmsKeyRequest.class) {
					if(access == AccessType.READ) {
						return true;
					} else {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

}
