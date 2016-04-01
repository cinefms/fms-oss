package com.openfms.core.service.project.impl.listeners;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsKeyRequestService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.crypto.FmsCertificate;
import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.crypto.FmsKeyRequest;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.core.playback.FmsPlaybackDevice;


@Component
public class KeyRequestListener extends FmsListenerAdapter<FmsKeyRequest> {
	
	private static Log log = LogFactory.getLog(KeyRequestListener.class);
	
	@Autowired
	private FmsKeyRequestService keyRequestService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	protected KeyRequestListener() {
	}

	@Override
	protected boolean beforeSave(String db, FmsKeyRequest kr) {

		FmsCertificate cert = null; 
				
		if(log.isDebugEnabled()){ log.debug("checking device ... "); }
		if(kr.getDeviceId()==null) {
			throw new RuntimeException("cannot do a key request without a device ID");
		} else {
			
			try {
				FmsPlaybackDevice dev = dataStore.getObject(FmsPlaybackDevice.class,kr.getDeviceId());
				if(dev==null) {
					kr.setDeviceName("[unknown]");
					kr.setCertificateDnQualifier(null);
					kr.setCertificateId(null);
					kr.setLocationId(null);
					kr.setLocationName(null);
					kr.setCanceled(true);
					kr.setTdlThumbprints(new ArrayList<String>());
				} else {
					kr.setDeviceName(dev.getName());
					if(dev.getCertificateId()==null) {
						throw new RuntimeException("unable to create a key request: device has no certificate!");
					}
	
					cert = dataStore.getObject(FmsCertificate.class,dev.getCertificateId());
					if(cert!=null) {
						kr.setCertificateId(cert.getId());
						kr.setCertificateDnQualifier(cert.getCertificateDnQualifier());
						kr.setTdlThumbprints(cert.getTdlThumbprints());
						
						FmsLocation loc = dataStore.getObject(FmsLocation.class, dev.getLocationId());
						kr.setLocationId(loc.getId());
						kr.setLocationName(loc.getName());
					} else {
						throw new RuntimeException("unable to create a key request: device has no certificate!");
					}
				}
				
			} catch (Exception e) {
				throw new RuntimeException("unable to save: "+e.getMessage(),e);
			}
		}
		
		if(log.isDebugEnabled()){ log.debug("checking media clip ... "); }
		if(kr.getMediaClipId()==null) {
			throw new RuntimeException("cannot do a key request without a media clip ID");
		} else {
			try {
				if(kr.getMediaClipId()==null) {
					kr.setCanceled(true);
				} else {
					FmsMediaClip mediaClip = dataStore.getObject(FmsMediaClip.class, kr.getMediaClipId());
					if(mediaClip==null) {
						kr.setCanceled(true);
					} else {
						if(!mediaClip.isEncrypted()) {
							kr.setCanceled(true);
						}
						kr.setMediaClipExternalId(mediaClip.getExternalId());
						kr.setMediaClipName(mediaClip.getName());
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("unable to save - error while setting media clip data: "+e.getMessage(),e);
			}
		}
		if(log.isDebugEnabled()){ log.debug("checking movie ... "); }
		if(kr.getMovieId()==null) {
			throw new RuntimeException("cannot do a key request without a movie ID");
		} else {
			try {
				FmsMovie movie = dataStore.getObject(FmsMovie.class, kr.getMovieId());
				kr.setMovieName(movie.getName());
				kr.setMovieId(movie.getId());

				
			} catch (Exception e) {
				throw new RuntimeException("unable to save - error while setting movie data: "+e.getMessage(),e);
			}
		}
		
		
		if(kr.getStart()==null) {
			throw new RuntimeException("cannot do a key request without a start time");
		}
		if(kr.getEnd()==null) {
			throw new RuntimeException("cannot do a key request without an end time");
		}

		
		if(log.isDebugEnabled()){ log.debug("checking existing key requests ... "); }
		try {
			DBStoreQuery q = BasicQuery.createQuery().eq("mediaClipId", kr.getMediaClipId());
			q = q.eq("deviceId", kr.getDeviceId());
			q = q.eq("start", kr.getStart());
			q = q.eq("end", kr.getEnd());
			q = q.gte("canceled", false);
			List<FmsKeyRequest> existing = dataStore.findObjects(FmsKeyRequest.class, q);
			if(existing.size()>0) {
				kr.setId(existing.get(0).getId());
			}
		} catch (Exception e) {
			throw new RuntimeException("unable to check for existing key requests: "+e.getMessage(),e);
		}
		
		if(log.isDebugEnabled()){ log.debug("checking existing keys ... "); }
		try {
			List<String> existing = new ArrayList<String>();  

			DBStoreQuery q = BasicQuery.createQuery();
			q = q.eq("mediaClipExternalId", kr.getMediaClipExternalId());
			q = q.eq("certificateDnQualifier", kr.getCertificateDnQualifier());
			List<FmsKey> keys = dataStore.findObjects(FmsKey.class, q); 
			if(log.isDebugEnabled()){ log.debug("existing keys: "+keys.size()+" ... "); }
			for(FmsKey k : keys) {
				if(k.getValidFrom().getTime() > Math.max(kr.getStart().getTime(),System.currentTimeMillis()-10000l)) {
					if(log.isDebugEnabled()){ log.debug("checking existing keys against keyrequest: key "+k.getId()+" starts too late"); }
				} else if(k.getValidTo().getTime() < kr.getEnd().getTime()) {
					if(log.isDebugEnabled()){ log.debug("checking existing keys against keyrequest: key "+k.getId()+" ends too early"); }
				} else if(!cert.matchesTDL(k)) {
					if(log.isDebugEnabled()){ log.debug("checking existing keys against keyrequest: key "+k.getId()+" doesn't cover the TDL"); }
				} else {
					if(log.isDebugEnabled()){ log.debug("checking existing keys against keyrequest: key "+k.getId()+" IS OK !!!"); }
					existing.add(k.getId());
				}
			}
			q = q.lte("start", kr.getStart());
			q = q.gte("end", kr.getEnd());

			kr.setFulfilled(existing.size()>0);
			kr.setKeyIds(existing);
		} catch (Exception e) {
			throw new RuntimeException("unable to check for existing key requests: "+e.getMessage(),e);
		}
		
		
		kr.setCreated(new Date());
		kr.setCreatedBy(FmsSessionHolder.getCurrentUser().getName());
		
		return true;
	}
	
	
	@Override
	protected void deleted(String db, FmsKeyRequest object) {
		updated(db, object, null);
	}

	@Override
	protected void created(String db, FmsKeyRequest object) {
		updated(db, null, object);
	}
	
	@Override
	protected void updated(String db, FmsKeyRequest o, FmsKeyRequest n) {
		try {
			for(FmsKeyRequest kr : new FmsKeyRequest[] { o, n } ) {
				if(kr==null) {
					continue;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("error updating dependent data",e);
		}
		
		
		super.updated(db, o, n);
	}
	
	

}
