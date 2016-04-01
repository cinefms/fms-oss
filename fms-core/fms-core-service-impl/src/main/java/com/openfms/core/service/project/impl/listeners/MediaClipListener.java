package com.openfms.core.service.project.impl.listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsCryptoKeyService;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMediaClipTaskService;
import com.openfms.core.service.project.FmsMediaClipTaskTypeService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.core.service.project.FmsPlaybackDeviceService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.movie.FmsMediaClipTask;
import com.openfms.model.core.movie.FmsMediaClipTaskType;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.core.movie.FmsMovieVersion;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.core.references.FmsEventReference;


@Component
public class MediaClipListener extends FmsListenerAdapter<FmsMediaClip> {
	
	private static Log log = LogFactory.getLog(MovieVersionListener.class);

	@Autowired
	private FmsMovieVersionService movieVersionService;
	
	@Autowired
	private FmsMovieService movieService;
	
	@Autowired
	private FmsMediaClipService mediaClipService;
	
	@Autowired
	private FmsEventService eventService;
	
	@Autowired
	private FmsCryptoKeyService keyService;
	
	@Autowired
	private FmsMediaClipTaskTypeService taskTypeService;
	
	@Autowired
	private FmsMediaClipTaskService taskService;
	
	@Autowired
	private FmsMoviePackageService packageService;

	@Autowired
	private FmsPlaybackDeviceService deviceService;
	
	
	@Autowired
	private ProjectDataStore dataStore;
	
	protected MediaClipListener() {
	}

	
	@Override
	protected void created(String db, FmsMediaClip object) {
		try {
			List<FmsMediaClipTaskType> tts = taskTypeService.list(BasicQuery.createQuery().eq("generateForMediaClipTypes", object.getType()));
			for(FmsMediaClipTaskType tt : tts) {
				FmsMediaClipTask t = new FmsMediaClipTask();
				t.setClosed(false);
				t.setCreated(new Date());
				t.setMediaClipId(object.getId());
				t.setType(tt.getId());
				t.setEvents(object.getEvents());
				if(tt.getDeviceId()!=null) {
					try {
						FmsPlaybackDevice device = deviceService.get(tt.getDeviceId());
						t.setDeviceId(device.getId());
						t.setDeviceName(device.getDisplayName());
					} catch (Exception e) {
						log.error("error setting device from existing task type in new task",e);
					}
				}
				taskService.save(t);
			}
			updated(db,null,object);
		} catch (Exception e) {
			throw new RuntimeException("error updating tasks", e);
		}
	}
	
	@Override
	protected boolean beforeSave(String db, FmsMediaClip object) {
		if(object.getMovieId()==null) {
			throw new RuntimeException("you cannot have a media clip without a movie ID");
		}
		try {
			
			
			
			FmsMovie m = null;
			if(object.getMovieId()!=null) {
				m = dataStore.getObject(FmsMovie.class, object.getMovieId());
			}
			object.setMovieId(m!=null?m.getId():null);
			object.setMovieName(m!=null?m.getName():null);
			object.setMovieExternalId(m!=null?m.getExternalId():null);
			object.setMovieCategory(m!=null?m.getCategory():null);
			
			List<FmsEventReference> references = new ArrayList<FmsEventReference>();
			Date nextEventDate = null;
			String nextEventId = null;
			
			if(object.getId()!=null) {
				for(FmsMovieVersion mv : movieVersionService.listMovieVersions(null, null, null, object.getId(), null, null, null, null)) {
					for(FmsEventReference fer : mv.getEvents()) {
						references.add(fer);
						if(fer.getScreeningDate().after(new Date())) {
							if(nextEventDate == null || nextEventDate.after(fer.getScreeningDate())) {
								nextEventDate = fer.getScreeningDate();
								nextEventId = fer.getEventId();
							}
						}
					}
				}
			}
			object.setNextEventDate(nextEventDate);
			object.setNextEventId(nextEventId);
			
			
			Collections.sort(references, new Comparator<FmsEventReference>() {
				@Override
				public int compare(FmsEventReference o1, FmsEventReference o2) {
					return o1.getScreeningDate().compareTo(o2.getScreeningDate());
				}
			});
			
			List<String> taskIds = new ArrayList<String>();
			int openTasks = 0;
			if(object.getId()!=null) {
				for(FmsMediaClipTask task : taskService.list(BasicQuery.createQuery().eq("mediaClipId", object.getId()))) {
					taskIds.add(task.getId());
					if(!task.isClosed()) {
						openTasks++;
					}
				}
			}
			object.setOpenTasks(openTasks);
			object.setTaskIds(taskIds);
			object.setEvents(references);
			Date f = null;
			if(references.size()>0) {
				f = references.get(0).getScreeningDate();
			}
			object.setFirstEventDate(f);
			
			List<String> keyIds = new ArrayList<String>();
			for(FmsKey key : keyService.list(BasicQuery.createQuery().eq("mediaClipExternalId",object.getExternalId()))) {
				keyIds.add(key.getId());
			}
			object.setKeyIds(keyIds);
			
			

		} catch (Exception e) {
			throw new RuntimeException("error setting derived data (before mediaclip save) ... ", e);
		}
		return true;
		
	}

	@Override
	protected void deleted(String db, FmsMediaClip object) {
		updated(db,object,null);
	}

	@Override
	protected void updated(String db, FmsMediaClip o, FmsMediaClip n) 	 {
		Set<String> mediaClipTaskIds = new TreeSet<String>(); 
		Set<String> movieVersionIds = new TreeSet<String>(); 
		Set<String> keyIds = new TreeSet<String>(); 
		Set<String> packageIds = new TreeSet<String>(); 
		Set<String> movieIds = new TreeSet<String>(); 
		try {
			for(FmsMediaClip object : new FmsMediaClip[] { o, n }) {
				if(object==null) {
					continue;
				}
				for(FmsMovieVersion mv : movieVersionService.list(BasicQuery.createQuery().in("mediaClipIds",object.getId()))) {
					movieVersionIds.add(mv.getId());
				}
				for(FmsKey key : keyService.list(BasicQuery.createQuery().eq("mediaClipExternalId",object.getExternalId()))) {
					keyIds.add(key.getId());
				}
				for(FmsMediaClipTask task : taskService.list(BasicQuery.createQuery().eq("mediaClipId", object.getId()))) {
					mediaClipTaskIds.add(task.getId());
				}
				packageIds.addAll(object.getMoviePackageIds());
			}
			if(log.isDebugEnabled()){ log.debug(" ### MediaClip updated ---> updating "+movieVersionIds.size()+" movie versions ... "); }
			for(String id : movieVersionIds) {
				movieVersionService.update(id);
			}
			if(log.isDebugEnabled()){ log.debug(" ### MediaClip updated ---> updating "+keyIds.size()+" keys ... "); }
			for(String id : keyIds) {
				keyService.update(id);
			}
			if(log.isDebugEnabled()){ log.debug(" ### MediaClip updated ---> updating "+mediaClipTaskIds.size()+" mediaclip tasks ... "); }
			for(String id : mediaClipTaskIds) {
				taskService.update(id);
			}
			if(log.isDebugEnabled()){ log.debug(" ### MediaClip updated ---> updating "+packageIds.size()+" packages ... "); }
			for(String id : packageIds) {
				packageService.update(id);
			}
			if(log.isDebugEnabled()){ log.debug(" ### MediaClip updated ---> updating "+movieIds.size()+" movies ... "); }
			for(String id : movieIds) {
				movieService.update(id);
			}
			
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data (media clip updated) ... ", e);
		}
	}
	
	
	@Override
	protected void beforeDelete(String db, FmsMediaClip object) {
		try {
			for(FmsMediaClipTask task : taskService.list(BasicQuery.createQuery().eq("mediaClipId", object.getId()))) {
				taskService.delete(task.getId());
			}
			
			for(FmsMovieVersion mv : dataStore.findObjects(FmsMovieVersion.class, BasicQuery.createQuery().in("mediaClipIds", object.getId()))) {
				mv.getMediaClipIds().remove(object.getId());
				movieVersionService.save(mv);
			}
			
		} catch (Exception e) {
			throw new RuntimeException("error deleting dependent data (before mediaclip delete) ... ", e);
		}
		super.deleted(db, object);
	}
	
	
}
