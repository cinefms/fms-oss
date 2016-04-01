package com.openfms.core.service.project.impl.listeners;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsLocationService;
import com.openfms.core.service.project.FmsPlaybackDeviceService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.crypto.FmsCertificate;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.core.references.FmsCertificateReference;


@Component
public class PlaybackDeviceListener extends FmsListenerAdapter<FmsPlaybackDevice> {

	@Autowired
	private FmsLocationService locationService;
	
	@Autowired
	private FmsPlaybackDeviceService playbackDeviceService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	
	protected PlaybackDeviceListener() {
	}

	
	@Override
	protected boolean beforeSave(String db, FmsPlaybackDevice object) {
		try {
			FmsCertificateReference cr = null;
			if(object.getCertificateId()!=null) {
				
				FmsCertificate cert = dataStore.getObject(FmsCertificate.class, object.getCertificateId());
				if(cert==null) {
					object.setCertificateId(null);
				} else {
					cr = new FmsCertificateReference(cert);
				}
			}
			object.setCertificate(cr);
			
			if(object.getLocationId()!=null) {
				FmsLocation loc = dataStore.getObject(FmsLocation.class, object.getLocationId());
				object.setLocationName(loc.getName());
			} else {
				object.setLocationName("");
			}
			
			
			
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data ... ", e);
		}
		return true;
		
	}

	@Override
	protected void deleted(String db, FmsPlaybackDevice object) {
		updated(db, object, null);
	}

	@Override
	protected void created(String db, FmsPlaybackDevice object) {
		updated(db, null, object);
	}
	
	
	@Override
	protected void updated(String db, FmsPlaybackDevice o, FmsPlaybackDevice n) {
		Set<String> locationIds = new TreeSet<String>(); 
		try {
			for(FmsPlaybackDevice pd : new FmsPlaybackDevice[] { o , n }) {
				if(pd==null) {
					continue;
				}
				if(pd!=null && pd.getLocationId()!=null) {
					locationIds.add(pd.getLocationId());						
				}
			}
			for(String id : locationIds) {
				locationService.update(id);
			}
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data ... ", e);
		}
		
		super.updated(db, o, n);
	}
	
}
