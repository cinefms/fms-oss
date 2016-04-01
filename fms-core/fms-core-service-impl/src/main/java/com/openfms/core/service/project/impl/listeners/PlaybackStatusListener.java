package com.openfms.core.service.project.impl.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsCryptoCertificateService;
import com.openfms.core.service.project.FmsCryptoKeyService;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsKeyRequestService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.playback.FmsEventPBS;


@Component
public class PlaybackStatusListener extends FmsListenerAdapter<FmsEventPBS> {
	
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
	
	protected PlaybackStatusListener() {
	}

	@Override
	protected void deleted(String db, FmsEventPBS object) {
		updated(db, object, null);
	}

	@Override
	protected void created(String db, FmsEventPBS object) {
		try {
			eventService.update(object.getEventId());
		} catch (Exception e) {
			throw new RuntimeException("error setting dependent data",e);
		}
		updated(db, null, object);
	}
	
	
	@Override
	protected boolean beforeSave(String db, FmsEventPBS pbs) {
		try {
		} catch (Exception e) {
			throw new RuntimeException("error setting drived data (before saving key) ... ",e);
		}
		return true;
	}
	
	
	@Override
	protected void updated(String db, FmsEventPBS o, FmsEventPBS n) {
	}
	

}
