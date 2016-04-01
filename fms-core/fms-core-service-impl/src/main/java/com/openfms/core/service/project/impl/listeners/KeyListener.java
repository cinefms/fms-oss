package com.openfms.core.service.project.impl.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsCryptoCertificateService;
import com.openfms.core.service.project.FmsCryptoKeyService;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsKeyRequestService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.crypto.FmsCertificate;
import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.crypto.FmsKeyRequest;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.core.references.FmsDeviceReference;


@Component
public class KeyListener extends FmsListenerAdapter<FmsKey> {
	
	@Autowired
	private FmsCryptoKeyService keyService;
	
	@Autowired
	private FmsKeyRequestService keyRequestService;
	
	@Autowired
	private FmsCryptoCertificateService certificateService;
	
	@Autowired
	private FmsEventService eventService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	protected KeyListener() {
	}

	@Override
	protected void deleted(String db, FmsKey object) {
		updated(db, object, null);
	}

	@Override
	protected void created(String db, FmsKey object) {
		updated(db, null, object);
	}
	
	
	@Override
	protected boolean beforeSave(String db, FmsKey k) {
		try {
			List<FmsDeviceReference> devices = new ArrayList<FmsDeviceReference>();
			FmsCertificate c = dataStore.findObject(FmsCertificate.class, BasicQuery.createQuery().eq("certificateDnQualifier", k.getCertificateDnQualifier()));
			if(c!=null) {
				k.setCertificateId(c.getId());
				List<FmsPlaybackDevice> pds = dataStore.findObjects(FmsPlaybackDevice.class, BasicQuery.createQuery().eq("certificate.certificateId", c.getId()));
				for(FmsPlaybackDevice pd : pds) {
					devices.add(new FmsDeviceReference(pd));
				}
				if(c!=null) {
					k.setCertificateId(c.getId());
				}
			}
			k.setDevices(devices);
		} catch (Exception e) {
			throw new RuntimeException("error setting drived data (before saving key) ... ",e);
		}
		return true;
	}
	
	
	@Override
	protected void updated(String db, FmsKey o, FmsKey n) {
		try {
			Set<String> keyRequestIds = new TreeSet<String>();
			Set<String> eventIds = new TreeSet<String>();
			for(FmsKey k : new FmsKey[] { o, n} ){
				if(k==null) {
					continue;
				}
				{
					DBStoreQuery q = BasicQuery.createQuery();
					q = q.eq("mediaClipExternalId",k.getMediaClipExternalId());
					q = q.eq("certificateDnQualifier",k.getCertificateDnQualifier());
					q = q.lte("end",k.getValidTo());
					q = q.gte("start",k.getValidFrom());
					for(FmsKeyRequest kr : dataStore.findObjects(FmsKeyRequest.class, q)) {
						keyRequestIds.add(kr.getId());
					}
				}
				{
					DBStoreQuery q = BasicQuery.createQuery();
					k.getMediaClipExternalId();
					q = q.in("devices.certificateDnQualifier", k.getCertificateDnQualifier());
					q = q.in("eventItems.mediaClips.mediaClipExternalId",k.getMediaClipExternalId());
					for(FmsEvent e : dataStore.findObjects(FmsEvent.class, q)) {
						eventIds.add(e.getId());
					}
				}
			}
			for(String id : keyRequestIds) {
				keyRequestService.update(id);
			}
			for(String id : eventIds) {
				eventService.update(id);
			}
			
		} catch (Exception e) {
			throw new RuntimeException("error updating dependent data when updating key",e);
		}
	}
	

}
