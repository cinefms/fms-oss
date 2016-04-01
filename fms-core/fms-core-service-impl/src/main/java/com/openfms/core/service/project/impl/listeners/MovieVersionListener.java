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
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.movie.FmsMovieVersion;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsEventItem;
import com.openfms.model.core.references.FmsEventReference;
import com.openfms.model.core.references.FmsMediaClipReference;


@Component
public class MovieVersionListener extends FmsListenerAdapter<FmsMovieVersion> {
	
	private static Log log = LogFactory.getLog(MovieVersionListener.class);
	
	@Autowired
	private FmsMovieService movieService;
	
	@Autowired
	private FmsMovieVersionService movieVersionService;
	
	@Autowired
	private FmsMediaClipService mediaClipService;
	
	@Autowired
	private FmsEventService eventService;
	
	protected MovieVersionListener() {
	}

	@Override
	protected boolean beforeSave(String db, FmsMovieVersion object) {
		try {
			List<FmsEventReference> reference = new ArrayList<FmsEventReference>();
			if(object.getId()!=null) {
				for(FmsEvent e : eventService.list(BasicQuery.createQuery().in("eventItems.movieVersionId", object.getId()))) {
					if(log.isDebugEnabled()){ log.debug("adding event to references in movie version ... "); }
					reference.add(new FmsEventReference(e));
				}
			}
			object.setEvents(reference);
			int mvLength = 0;
			List<FmsMediaClipReference> mcRefs = new ArrayList<FmsMediaClipReference>();
			if(object.getMediaClipIds()!=null) {
				for(String mcId : object.getMediaClipIds()) {
					FmsMediaClip mc = mediaClipService.get(mcId);
					mcRefs.add(new FmsMediaClipReference(mc));
					mvLength = mvLength + mc.getLength();
					if(log.isDebugEnabled()){ log.debug("adding mediaclip to references to movie version ... status: "+mc.getStatus()); }
				}
			}
			object.setMediaClips(mcRefs);
			object.setLength(mvLength);
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data ... ", e);
		}
		return true;
		
	}

	@Override
	protected void created(String db, FmsMovieVersion object) {
		updated(db,null,object);
	}

	@Override
	protected void deleted(String db, FmsMovieVersion object) {
		updated(db,object,null);
	}

	@Override
	protected void updated(String db, FmsMovieVersion o, FmsMovieVersion n) {
		Set<String> movieIds = new TreeSet<String>(); 
		Set<String> mediaClipIds = new TreeSet<String>(); 
		Set<String> eventIds = new TreeSet<String>(); 
		try {
			for(FmsMovieVersion mv : new FmsMovieVersion[] { o , n }) {
				if(mv!=null) {
					for(FmsEvent e : eventService.list(BasicQuery.createQuery().eq("eventItems.movieVersionId",mv.getId()))) {
						eventIds.add(e.getId());
					}
					if(mv.getMediaClipIds()!=null) {
						mediaClipIds.addAll(mv.getMediaClipIds());
					}
					movieIds.add(mv.getMovieId());
				}
			}
			if(log.isDebugEnabled()){ log.debug(" ### MovieVersion updated ---> updating "+mediaClipIds.size()+" mediaclips ... "); }
			for(String id : mediaClipIds) {
				mediaClipService.update(id);
			}
			if(log.isDebugEnabled()){ log.debug(" ### MovieVersion updated ---> updating "+eventIds.size()+" events ... "); }
			for(String id : eventIds) {
				eventService.update(id);
			}
			if(log.isDebugEnabled()){ log.debug(" ### MovieVersion updated ---> updating "+movieIds.size()+" movies ... "); }
			for(String id : movieIds) {
				movieService.update(id);
			}
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data ... ", e);
		}
	}

	@Override
	protected void beforeDelete(String db, FmsMovieVersion object) {
		try {
			for(FmsEvent event : eventService.listEvents(null, null, null, null, null, null, null, null, null, null, null, null, null, object.getId(), null, null, null, null, null, true, null, null)) {
				for(FmsEventItem fei : event.getEventItems()) {
					if(fei.getMovieVersionId()!=null || fei.getMovieVersionId().compareTo(object.getId())==0) {
						fei.setMovieVersionId(null);
					}
				}
				eventService.save(event);
			}
		} catch (Exception e) {
			throw new RuntimeException("unable to update dependent data: ",e);
		}
	}
	

}
