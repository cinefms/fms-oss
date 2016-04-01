package com.openfms.core.service.project.impl.listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMoviePackageCopyService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.core.movie.FmsMoviePackage;
import com.openfms.model.core.movie.FmsMovieVersion;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsEventItem;
import com.openfms.model.core.references.FmsEventReference;
import com.openfms.model.core.storage.FmsMoviePackageCopy;
import com.openfms.model.utils.StatusCombine;


@Component
public class MovieListener extends FmsListenerAdapter<FmsMovie> {
	
	
	private static Log log = LogFactory.getLog(MovieListener.class); 
	
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
	private ProjectDataStore dataStore;
	
	protected MovieListener() {
	}
	
	@Override
	protected boolean beforeSave(String db, FmsMovie object) {
		try {
			List<FmsEventReference> references = new ArrayList<FmsEventReference>();
			
			Date nextEventDate = null;
			String nextEventId = null;
			
			if(object.getId()!=null) {
				
				if(log.isDebugEnabled()){ log.debug("MOVIE LISTENER = BEFORE SAVE: "+object.getId()); }
				
				int ms = -1;
				int vs = -1;
				int es = -1;
				int ps = -1;

				List<FmsMovieVersion> versions = dataStore.findObjects(FmsMovieVersion.class, BasicQuery.createQuery().eq("movieId", object.getId()));
				object.setNumVersion(versions.size());
				
				List<FmsMediaClip> clips = dataStore.findObjects(FmsMediaClip.class, BasicQuery.createQuery().eq("movieId", object.getId()));
				object.setNumClips(clips.size());
				
				List<FmsEvent> events = dataStore.findObjects(FmsEvent.class, BasicQuery.createQuery().eq("eventItems.movieId", object.getId()));
				object.setNumEvents(events.size());
				
				if(log.isDebugEnabled()){ log.debug("MOVIE LISTENER = BEFORE SAVE: "+events.size()+" events "); }
				for(FmsEvent e : events) {
					
					if(e.getStartTime().after(new Date())) {
						if(nextEventDate == null || e.getStartTime().getTime()<nextEventDate.getTime()) {
							nextEventDate = e.getStartTime();
							nextEventId = e.getId();
						}
					}
					
					if(log.isDebugEnabled()){ log.debug("MOVIE LISTENER = BEFORE SAVE: "+events.size()+" events: checking "+e.getId()); }
					references.add(new FmsEventReference(e));
					for(FmsEventItem fei : e.getEventItems()) {
						if(fei.getMovieId().compareTo(object.getId())==0) {
							ms = StatusCombine.combine(ms,fei.getMediaStatus());
							vs = StatusCombine.combine(vs,fei.getVersionStatus());
							es = StatusCombine.combine(es,fei.getEncryptionStatus());
							ps = StatusCombine.combine(ps,fei.getPlaybackStatus());
						}
					}
				}
				object.setNextEventDate(nextEventDate);
				object.setNextEventId(nextEventId);
				object.setMediaStatus(ms);
				object.setVersionStatus(vs);
				object.setEncryptionStatus(es);
				object.setPlaybackStatus(ps);
			}
			
			Collections.sort(references, new Comparator<FmsEventReference>() {
				@Override
				public int compare(FmsEventReference o1, FmsEventReference o2) {
					return o1.getScreeningDate().compareTo(o2.getScreeningDate());
				}
			});
			
			Date f = null;
			if(references.size()>0) {
				f = references.get(0).getScreeningDate();
			}
			object.setFirstEventDate(f);
			
			object.setEvents(references);
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data ... ", e);
		}
		return true;
		
	}
	
	@Override
	protected void beforeDelete(String db, FmsMovie object) {
		try {
			for(FmsEvent e : eventService.listEvents(null, null, null, null, null, null, null, null, null, null, null, null, object.getId(), null, null, null, null, null, null, true, null, null)) {
				List<FmsEventItem> feiR = new ArrayList<FmsEventItem>();
				for(FmsEventItem fei : e.getEventItems()) {
					if(fei.getMovieId().compareTo(object.getId())==0) {
						feiR.add(fei);
					}
				}
				if(feiR.size()>0) {
					e.getEventItems().removeAll(feiR);
					eventService.save(e);
				}
			}
			
			for(FmsMovieVersion mv : movieVersionService.list(BasicQuery.createQuery().eq("movieId", object.getId()))) {
				movieVersionService.delete(mv.getId());
			}

			for(FmsMediaClip mc : mediaClipService.list(BasicQuery.createQuery().eq("movieId", object.getId()))) {
				mediaClipService.delete(mc.getId());
			}
			
			for(FmsMoviePackage mp : moviePackageService.list(BasicQuery.createQuery().eq("movieId", object.getId()))) {
				mediaClipService.delete(mp.getId());
			}
			
			for(FmsMoviePackageCopy mpc : moviePackageCopyService.list(BasicQuery.createQuery().eq("movieId", object.getId()))) {
				moviePackageCopyService.delete(mpc.getId());
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException("error deleting or updating dependent data ... ", e);
		}
		
	}


}
