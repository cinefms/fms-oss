package com.openfms.core.service.project.impl.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsLocationService;
import com.openfms.core.service.project.FmsPlaybackDeviceService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.core.playback.FmsSite;
import com.openfms.model.core.references.FmsCertificateReference;
import com.openfms.model.core.references.FmsDeviceReference;


@Component
public class LocationListener extends FmsListenerAdapter<FmsLocation> {

	private static Log log = LogFactory.getLog(LocationListener.class);
	
	
	@Autowired
	private FmsLocationService locationService;
	
	@Autowired
	private FmsPlaybackDeviceService playbackDeviceService;
	
	@Autowired
	private FmsEventService eventService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	
	protected LocationListener() {
	}

	
	@Override
	protected boolean beforeSave(String db, FmsLocation object) {
		
		try {
			if(object.getSiteId()!=null) {
				FmsSite site = dataStore.getObject(FmsSite.class, object.getSiteId());
				if(site==null) {
					object.setSiteId(null);
					object.setSiteName("");
				} else {
					object.setSiteName(site.getName());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data (site name)... ", e);
		}
		
		try {
			List<FmsCertificateReference> cRefs = new ArrayList<FmsCertificateReference>();
			List<FmsDeviceReference> pdRefs = new ArrayList<FmsDeviceReference>();
			if(object.getId()!=null) {
				List<FmsPlaybackDevice> devices = dataStore.findObjects(FmsPlaybackDevice.class, BasicQuery.createQuery().eq("locationId", object.getId()));
				for(FmsPlaybackDevice pd : devices) {
					pdRefs.add(new FmsDeviceReference(pd));
					if(pd.getCertificate()!=null) {
						cRefs.add(pd.getCertificate());
					}
				}
			}

			object.setDevices(pdRefs);
			object.setCertificates(cRefs);
			
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data (certificate/device references)... ", e);
		}
		return true;
		
	}

	@Override
	protected void beforeDelete(String db, FmsLocation object) {
		try {
			List<FmsPlaybackDevice> devices = dataStore.findObjects(FmsPlaybackDevice.class, BasicQuery.createQuery().eq("locationId", object.getId()));
			for(FmsPlaybackDevice pd : devices) {
				pd.setLocationId(null);
				playbackDeviceService.save(pd);
			}
		} catch (Exception e) {
			throw new RuntimeException("error deleting dependent data (playback devices) ... ", e);
		}
		try {
			List<FmsEvent> events= dataStore.findObjects(FmsEvent.class, BasicQuery.createQuery().eq("locationId", object.getId()));
			for(FmsEvent e : events) {
				e.setLocationId(null);
				eventService.save(e);
			}
		} catch (Exception e) {
			throw new RuntimeException("error deleting dependent data (events) ... ", e);
		}
		super.deleted(db, object);
	}
	

	@Override
	protected void deleted(String db, FmsLocation object) {
		updated(db, object, null);
	}

	@Override
	protected void created(String db, FmsLocation object) {
		updated(db, null, object);
	}
	
	
	@Override
	protected void updated(String db, FmsLocation o, FmsLocation n) {
		try {
			Set<String> eventIds = new TreeSet<String>(); 
			for(FmsLocation l : new FmsLocation[] { o,n }) {
				if(l==null) {
					continue;
				}
				for(FmsEvent e : dataStore.findObjects(FmsEvent.class, BasicQuery.createQuery().eq("locationId", l.getId()))) {
					eventIds.add(e.getId());
				}
			}
			log.info(" LOCATION CHANGED! Updating "+eventIds.size()+" events ... ");
			for(String eventId : eventIds) {
				eventService.update(eventId);
			}
		} catch (Exception e) {
			throw new RuntimeException("error updating derived data",e);
		}
	}
	
	
}
