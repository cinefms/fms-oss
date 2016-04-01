package com.openfms.core.service.project.impl.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsLocationService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.core.playback.FmsSite;


@Component
public class SiteListener extends FmsListenerAdapter<FmsSite> {

	@Autowired
	private FmsLocationService locationService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	
	protected SiteListener() {
	}

	@Override
	protected void created(String db, FmsSite object) {
	}

	@Override
	protected void deleted(String db, FmsSite object) {
		try {
			for(FmsLocation loc : dataStore.findObjects(FmsLocation.class,BasicQuery.createQuery().eq("siteId", object.getId())) ) {
				loc.setSiteName(null);
				loc.setSiteId(null);
				locationService.save(loc);
			}
		} catch (Exception e) {
			throw new RuntimeException("error updating dependencies",e);
		}
	}

	@Override
	protected void updated(String db, FmsSite o, FmsSite n) {
		if(n==null) {
			return;
		}
		try {
			for(FmsLocation loc : dataStore.findObjects(FmsLocation.class,BasicQuery.createQuery().eq("siteId", n.getId())) ) {
				loc.setSiteName(n.getName());
				locationService.save(loc);
			}
		} catch (Exception e) {
			throw new RuntimeException("error updating dependencies",e);
		}
	}

	
}
