package com.openfms.core.service.project.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.api.DBStoreBinary;
import com.cinefms.dbstore.api.impl.BasicBinary;
import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsCryptoKeyService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsProjectUser;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.crypto.FmsKeyData;
import com.openfms.model.core.crypto.FmsKeyParseResult;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;
import com.openfms.model.exceptions.VersioningException;
import com.openfms.model.spi.crypto.KeyParser;

@Service
public class FmsCryptoKeyServiceImpl extends GenericProjectServiceImpl<FmsKey> implements FmsCryptoKeyService {

	private static final String KEY_DATA_BUCKET = "KeyData";

	private static Log log = LogFactory.getLog(FmsCryptoKeyServiceImpl.class);
	
	@Autowired
	private KeyParser keyParser;
	
	@Override
	public List<FmsKey> listKeys(String searchTerm, String externalId, String keyId, String movieId, String movieVersionId, String mediaClipId, String mediaClipExternalId, String issuerDnQualifier, String certificateDnQualifier, String certificateId, String deviceId,
			String eventId, Date validAt, String order, boolean asc, int start, int max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (deviceId != null) {
			q = q.in("devices.deviceId", deviceId);
		}
		if (searchTerm != null) {
			q = q.contains("titleText", searchTerm);
		}
		if (eventId != null) {
			q = q.in("events.eventId", eventId);
		}
		if (keyId != null) {
			q = q.eq("_id", keyId);
		}
		if (externalId != null) {
			q = q.eq("externalId", externalId);
		}
		if (mediaClipExternalId != null) {
			q = q.eq("mediaClipExternalId", mediaClipExternalId);
		}
		if (mediaClipId != null) {
			q = q.eq("mediaClipId", mediaClipId);
		}
		if (movieId != null) {
			q = q.eq("movieId", movieId);
		}
		if (issuerDnQualifier != null) {
			q = q.eq("issuerDnQualifier", issuerDnQualifier);
		}
		if (certificateDnQualifier != null) {
			q = q.eq("certificateDnQualifier", certificateDnQualifier);
		}
		if (certificateId != null) {
			q = q.eq("certificateId", certificateId);
		}
		if (validAt != null) {
			q = q.lte("validFrom", validAt);
			q = q.gte("validTo", validAt);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start);
		q = q.max(max);
		return list(q);
	}

	@Override
	public FmsKey save(FmsKey t) throws AccessDeniedException, DatabaseException, VersioningException {
		List<FmsKey> keys = dataStore.findObjects(FmsKey.class, BasicQuery.createQuery().eq("externalId", t.getExternalId()));
		if(keys.size() > 0) {
			t.setId(keys.get(0).getId());
			t.setVersion(keys.get(0).getVersion());
		}
		return super.save(t);
	}
	
	@Override
	public DBStoreBinary getData(String keyId) throws EntityNotFoundException, DatabaseException, InvalidParameterException, AccessDeniedException {
		if(!authzService.allowAccess(FmsKeyData.class, keyId, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		try {
			return dataStore.getBinary(KEY_DATA_BUCKET, keyId);
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void setData(DBStoreBinary data) throws EntityNotFoundException, DatabaseException, InvalidParameterException, AccessDeniedException {
		if(!authzService.allowAccess(FmsKeyData.class, data.getId(), AccessType.UPDATE)) {
			throw new AccessDeniedException();
		}
		try {
			FmsKey key = dataStore.getObject(FmsKey.class, data.getId());
			dataStore.saveBinary(data, KEY_DATA_BUCKET);
			DBStoreBinary b = dataStore.getBinary(KEY_DATA_BUCKET, data.getId());
			key.setSize((int)b.getLength());
			if(key.getSize()<1) {
				throw new RuntimeException("saved binary was 0 bytes or smaller - this is wrong!");
			}
			dataStore.saveObject(key);
		} catch (Exception e) {
			try {
				dataStore.deleteObject(FmsKey.class, data.getId());
			} catch (Exception e2) {
			}
			throw new DatabaseException(e);
		}
	
	}

	
	@Override
	public List<FmsKeyParseResult> upload(String movieId, String filename, byte[] bytes, String source) throws IOException, AccessDeniedException {
		boolean access = false;
		if(authzService.allowAccess(FmsKeyData.class, AccessType.CREATE)) {
			// ok 
			access = true;
		} else if(FmsSessionHolder.getCurrentUser() instanceof FmsProjectUser) {
			FmsProjectUser fpu = (FmsProjectUser)FmsSessionHolder.getCurrentUser();
			if(fpu.getMovieIds()!=null && fpu.getMovieIds().contains(movieId)) {
				access = true;
			}
		}
		if(!access) {
			throw new AccessDeniedException(FmsKeyData.class);
		}
		
		
		log.info(" KDM UPLOAD : "+FmsSessionHolder.getCurrentUser().getDisplayName()+" is uploading "+bytes.length+" bytes ... " );
		List<FmsKeyParseResult> res = keyParser.parseKdms(source+" (user:"+FmsSessionHolder.getCurrentUser().getName()+")",filename, movieId, bytes); 
		log.info(" KDM UPLOAD : "+FmsSessionHolder.getCurrentUser().getDisplayName()+" is uploading "+bytes.length+" bytes: "+res.size()+" potential files" );
		for(FmsKeyParseResult r : res) {
			log.info(" KDM UPLOAD : "+FmsSessionHolder.getCurrentUser().getDisplayName()+": "+r.getFilename()+" / "+r.isOk());
			if(r.isOk()) {
				try {
					log.info(" KDM UPLOAD : "+FmsSessionHolder.getCurrentUser().getDisplayName()+" KEY DATA ========================== ");
					log.info(new String(r.getData(),"utf-8"));
					log.info(" KDM UPLOAD : "+FmsSessionHolder.getCurrentUser().getDisplayName()+" saving key metadata ...  ");
					
					FmsKey k = r.getKey();
					if(k.getExternalId()==null || k.getExternalId().trim().length()==0) {
						throw new RuntimeException("no external ID");
					}
					
					DBStoreQuery q = BasicQuery.createQuery();
					q = q.eq("externalId", k.getExternalId());
					q = q.eq("md5", k.getMd5());
					
					List<FmsKey> keysOld = dataStore.findObjects(FmsKey.class, q);
					if(keysOld.size()==1) {
						k.setId(keysOld.get(0).getId());
					}
					k = dataStore.saveObject(r.getKey());
					if(k.getId()==null) {
						throw new RuntimeException("unable to save key!");
					}
					log.info(" KDM UPLOAD : "+FmsSessionHolder.getCurrentUser().getDisplayName()+" saving key data ...  ");
					DBStoreBinary b = new BasicBinary(k.getId(),r.getData());
					dataStore.saveBinary(b, KEY_DATA_BUCKET);
					log.info(" KDM UPLOAD : "+FmsSessionHolder.getCurrentUser().getDisplayName()+" loading key data ...  ");
					b = dataStore.getBinary(KEY_DATA_BUCKET, k.getId());
					log.info(" KDM UPLOAD : "+FmsSessionHolder.getCurrentUser().getDisplayName()+" key data size: "+b.getLength());
					k.setSize((int)b.getLength());
					k = dataStore.saveObject(k, true, false);
					r.setKey(k);
					
				} catch (Exception e) {
					r.setOk(false);
					log.error("error saving key: ",e);
				}
			}
		}
		return res;
	}
	
	
}
