package com.openfms.core.service.project.impl.listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMediaClipTaskService;
import com.openfms.core.service.project.FmsPlaybackDeviceService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.auth.FmsProjectUser;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.movie.FmsMediaClipTask;
import com.openfms.model.core.movie.FmsMediaClipTaskProgress;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.core.references.FmsEventReference;
import com.openfms.model.exceptions.EntityNotFoundException;


@Component
public class MediaClipTaskListener extends FmsListenerAdapter<FmsMediaClipTask> {
	
	private static Log log = LogFactory.getLog(MediaClipTaskListener.class);
	
	@Autowired
	private FmsMediaClipService mediaClipService;
	
	@Autowired
	private FmsEventService eventService;
	
	@Autowired
	private FmsMediaClipTaskService mediaClipTaskService;

	@Autowired
	private FmsPlaybackDeviceService playbackDeviceService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	
	protected MediaClipTaskListener() {
	}

	@Override
	protected boolean beforeSave(String db, FmsMediaClipTask object) {
		try {
			FmsProjectUser u = null;
			if(object.getUserId()!=null) {
				u = dataStore.getObject(FmsProjectUser.class, object.getUserId());
			}
			object.setUserId(u==null?null:u.getId());
			object.setUserName(u==null?null:u.getName());
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data ... ", e);
		}
		try {
			if(object.getDeviceId()!=null) {
				FmsPlaybackDevice d = playbackDeviceService.get(object.getDeviceId());
				object.setDeviceId(d.getId());
				object.setDeviceName(d.getDisplayName());
			}
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data ... ", e);
		}
		try {
			object.getEvents().clear();
			FmsMediaClip clip = null;
			List<FmsEventReference> references = new ArrayList<FmsEventReference>();
			try {
				clip = mediaClipService.get(object.getMediaClipId());
			} catch (EntityNotFoundException enfe) {
				// nothing to do ... just remove
			}
			if(clip!=null) {
				if(clip.isDisabled()) {
					log.info("settings task to CLOSED because the clip is disable!");
					object.setClosed(true);
				}
				object.setName(clip.getName());
				object.setMediaClipStatus(clip.getStatus());
				object.setMediaClipType(clip.getType());
				object.setMediaClipName(clip.getName());
				object.setMediaClipEncryptionStatus(clip.isEncrypted());
				references.addAll(clip.getEvents());
				if(clip.isDisabled()) {
					object.setClosed(true);
				}
			}
			Collections.sort(references, new Comparator<FmsEventReference>() {
				@Override
				public int compare(FmsEventReference o1, FmsEventReference o2) {
					return o1.getScreeningDate().compareTo(o2.getScreeningDate());
				}
			});
			if(references.size()>0) {
				object.setFirstEventDate(references.get(0).getScreeningDate());
			} else {
				object.setFirstEventDate(null);
			}
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data ... ", e);
		}
		return true;
		
	}

	@Override
	protected void created(String db, FmsMediaClipTask object) {
		updated(db,null,object);
	}

	@Override
	protected void deleted(String db, FmsMediaClipTask object) {
		updated(db,object,null);
	}

	@Override
	protected void updated(String db, FmsMediaClipTask o, FmsMediaClipTask n) {
		for(FmsMediaClipTask t : new FmsMediaClipTask[] { o, n }) {
			if(t==null || t.getId()==null) {
				continue;
			}
			try {
				if(t.getMediaClipId()!=null) {
					mediaClipService.update(t.getMediaClipId());
				}
			} catch (Exception e) {
				throw new RuntimeException("unable to update media clip (after media clip task updated)",e);
			} 
		}
		super.updated(db, o, n);
	}
	
	@Override
	protected void beforeDelete(String db, FmsMediaClipTask object) {
		try {
			for(FmsMediaClipTaskProgress progress : mediaClipTaskService.getProgress(object.getId())) {
				dataStore.deleteObject(progress);
			}
			super.beforeDelete(db, object);
		} catch (Exception e) {
			//throw new RuntimeException("error deleting dependent data ... ", e);
		}
	}
	
	
}
