package com.openfms.core.service.project.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openfms.core.service.project.FmsCryptoKeyService;
import com.openfms.core.service.project.FmsKeyRequestService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.crypto.FmsKeyData;
import com.openfms.model.core.crypto.FmsKeyParseResult;
import com.openfms.model.core.crypto.FmsKeyRequest;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;
import com.openfms.model.spi.crypto.KeyParser;

@Service
public class FmsKeyRequestServiceImpl extends GenericProjectServiceImpl<FmsKeyRequest> implements FmsKeyRequestService {
	
	private static Log log = LogFactory.getLog(FmsKeyRequestServiceImpl.class);
	
	@Autowired
	private KeyParser keyParser;
	
	@Autowired
	private FmsCryptoKeyService keyService;
	
	@Override
	public void fulfillKeyRequest(String id, byte[] key) throws AccessDeniedException, DatabaseException, EntityNotFoundException, IOException, VersioningException {
		if(!authzService.allowAccess(FmsKeyData.class, AccessType.CREATE)) {
			throw new AccessDeniedException();
		}
		FmsKeyRequest kr = dataStore.getObject(FmsKeyRequest.class, id);
		if(kr==null) {
			throw new EntityNotFoundException(FmsKeyRequest.class,id);
		}
		FmsMovie movie = dataStore.getObject(FmsMovie.class, kr.getMovieId());
		FmsEvent event = null;
		if(kr.getEventId()!=null) {
			event = dataStore.getObject(FmsEvent.class, kr.getEventId());
		}
		
		StringBuffer sb = new StringBuffer();
		if(event!=null) {
			sb.append(event.getExternalId()+"_"+event.getName());
			sb.append("_");
		}
		sb.append("KDM_");
		sb.append(movie.getExternalId());
		sb.append("_");
		sb.append(movie.getName());
		sb.append("_");
		sb.append(kr.getLocationName());
		sb.append(".xml");
	
		String filename = sb.toString().replaceAll("[^a-zA-Z0-9._-]+", "");
		
		List<FmsKeyParseResult> res = keyParser.parseKdms("KDM Generator", filename, movie.getId(), key);
		
		if(res.size()!=1) {
			throw new RuntimeException("Error parsing KDM from key request  - size should be 1, but it is: "+res.size());
		}
 		
		FmsKey fKey = null;
		
		
		for(FmsKeyParseResult kpr : keyService.upload(movie.getId(), sb.toString(), key, "KDM Generator")) {
			fKey = kpr.getKey();
		}

		if(fKey==null) {
			throw new RuntimeException("unable to extract key from upload");
		}
		
		kr = dataStore.getObject(FmsKeyRequest.class, id);
		
		kr.setFulfilled(true);
		kr.setFulfilledBy(fKey.getId());
		kr.setKeyId(fKey.getId());

		kr = dataStore.saveObject(kr);
		
		log.info(" ---> KEY "+fKey.getId()+" --- fulfilled key request: "+kr.getId()+" / "+kr.isFulfilled());
		
		
	}
	
	

}
