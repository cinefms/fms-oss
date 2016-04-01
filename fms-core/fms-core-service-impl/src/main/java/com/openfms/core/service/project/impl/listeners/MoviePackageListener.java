package com.openfms.core.service.project.impl.listeners;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsFileService;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMoviePackageCopyService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.movie.FmsFile;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.movie.FmsMoviePackage;
import com.openfms.model.core.references.FmsEventReference;
import com.openfms.model.core.storage.FmsMoviePackageCopy;


@Component
public class MoviePackageListener extends FmsListenerAdapter<FmsMoviePackage> {
	
	@Autowired
	private FmsMovieVersionService movieVersionService;
	
	@Autowired
	private FmsEventService eventService;
	
	@Autowired
	private FmsMediaClipService mediaClipService;
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	@Autowired
	private FmsMoviePackageCopyService moviePackageCopyService;
	
	@Autowired
	private FmsFileService fileService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	protected MoviePackageListener() {
	}
	
	@Override
	protected boolean beforeSave(String db, FmsMoviePackage object) {
		try {
			List<FmsEventReference> events = new ArrayList<FmsEventReference>();
			if(object.getId()!=null) {
				for(FmsMediaClip mc : dataStore.findObjects(FmsMediaClip.class, BasicQuery.createQuery().in("moviePackageIds", object.getId()))) {
					for(FmsEventReference er : mc.getEvents()) {
						if(!events.contains(er)) {
							events.add(er);
						}
					}
				}
			}
			
			object.setEvents(events);
			
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data: event references in movie packages",e);
		}
		return super.beforeSave(db, object);
	}
	
	@Override
	protected void beforeDelete(String db, FmsMoviePackage object) {
		try {
			
			for(FmsMediaClip mc : mediaClipService.list(BasicQuery.createQuery().eq("moviePackageIds", object.getId()))) {
				mediaClipService.delete(mc.getId());
			}
			
			for(FmsFile f : fileService.list(BasicQuery.createQuery().eq("packageId", object.getId()))) {
				fileService.delete(f.getId());
			}
			
			for(FmsMoviePackageCopy mpc : moviePackageCopyService.list(BasicQuery.createQuery().eq("moviePackageId", object.getId()))) {
				moviePackageCopyService.delete(mpc.getId());
			}
			
		} catch (Exception e) {
			throw new RuntimeException("error deleting or updating dependent data ... ", e);
		}
		
	}


}
