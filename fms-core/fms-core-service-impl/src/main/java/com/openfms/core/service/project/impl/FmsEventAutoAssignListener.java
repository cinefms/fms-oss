package com.openfms.core.service.project.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsLocationService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.model.core.auth.FmsSession;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.movie.FmsMovieVersion;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsEventAutoAssign;
import com.openfms.model.core.playback.FmsEventAutoAssignLog;
import com.openfms.model.core.playback.FmsEventAutoAssignLogItem;
import com.openfms.model.core.playback.FmsEventItem;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectHolder;
import com.openfms.model.core.references.FmsDeviceReference;
import com.openfms.model.core.references.FmsMediaClipReference;
import com.openfms.model.core.references.FmsMovieVersionReference;
import com.openfms.model.utils.ListenerTrackerHolder;

@Service
public class FmsEventAutoAssignListener extends FmsListenerAdapter<FmsEventAutoAssign>  implements ApplicationListener<ContextClosedEvent> {

	private static Log log = LogFactory.getLog(FmsEventAutoAssignListener.class);
	
	@Autowired
	private FmsEventService eventService;
	
	@Autowired
	private FmsMovieVersionService versionService;
	
	@Autowired
	private FmsLocationService locationService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	private ScheduledExecutorService e = Executors.newScheduledThreadPool(1);
	
	@Override
	public void created(String db, FmsEventAutoAssign o) {
		e.schedule(new AssignRunnable(o), 1, TimeUnit.SECONDS);
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent arg0) {
		try {
			e.shutdownNow();
		} catch (Exception e) {
		}
	}
	
	private class AssignRunnable implements Runnable {
		
		private FmsEventAutoAssign autoAssign;
		private FmsUser user;
		private FmsProject project;
		
		public AssignRunnable(FmsEventAutoAssign a) {
			this.user = FmsSessionHolder.getCurrentUser();
			this.project = FmsProjectHolder.get();
			this.autoAssign = a;
		}
		

		private FmsEventAutoAssign save(FmsEventAutoAssign a) {
			try {
				ListenerTrackerHolder.clear();
				a = dataStore.saveObject(a,true,false);
			} catch (Exception e) {
				log.error("error saving status: ",e);
			}
			return a;
		}
		
		private boolean matches(List<String> a, List<String> b) {
			if(a==null && b!=null) {
				return false;
			}
			if(a!=null && b==null) {
				return false;
			}
			if(a==null && b==null) {
				return true;
			}
			return (StringUtils.join(a,",").compareTo(StringUtils.join(b,",")))==0;
		}
		
		private boolean matches(FmsMovieVersion mv, FmsEventItem fei) {
			if(mv.isDisabled()) {
				return false;
			}
			if(autoAssign.isCheckSubtitleLanguage()) {
				if(!matches(mv.getSubtitleLanguageIds(),fei.getSubtitleLanguageIds())) {
					return false;
				}
			}
			if(autoAssign.isCheckAudioLanguage()) {
				if(!matches(mv.getAudioLanguageIds(),fei.getAudioLanguageIds())) {
					return false;
				}
			}
			return true;
		}

		private boolean playable(FmsLocation loc, FmsMovieVersion v) {
			if(loc==null && v!=null) {
				return false;
			}
			if(loc!=null && v==null) {
				return false;
			}
			if(loc==null && v==null) {
				return true;
			}
			for(FmsMediaClipReference mc : v.getMediaClips()) {
				boolean ok = false;
				for(FmsDeviceReference fdr : loc.getDevices()) {
					if(fdr.getMediaClipTypes().contains(mc.getMediaClipType())) {
						ok = true;
						log.debug("playable / media clip "+mc.getMediaClipType()+" / "+StringUtils.join(fdr.getMediaClipTypes(),", "));
						break;
					} else {
						log.debug("NOT playable / media clip "+mc.getMediaClipType()+" / "+StringUtils.join(fdr.getMediaClipTypes(),", "));
					}
				}
				if(!ok) {
					log.debug("not playable / media clip "+mc.getMediaClipType()+" / ");
					return false;
				}
			}
			return true;
		}
		
		@Override
		public void run() {
			FmsSession s = new FmsSession();
			s.setUser(user);
			FmsSessionHolder.set(s);
			FmsProjectHolder.set(project);

			try {
				DBStoreQuery q = BasicQuery.createQuery();
				List<FmsEvent> events = eventService.list(q);
				log.info(" ============================================================================== ");
				log.info(" == ");
				log.info(" == STARTING TO CHECK AUTO-ASSIGN FOR "+events.size()+" EVENTS ");
				log.info(" == ");
				log.info(" ============================================================================== ");
				autoAssign.setStartTime(new Date());
				autoAssign = save(autoAssign);
				for(FmsEvent fe : events) {
					FmsEventAutoAssignLog l = new FmsEventAutoAssignLog();
					l.setEventDate(fe.getStartTime());
					l.setEventName(fe.getName());
					l.setEventId(fe.getId());
					autoAssign.getLog().add(l);
					try {
						FmsLocation loc = locationService.get(fe.getLocationId());
						if(fe.getStartTime().before(new Date())) {
							l.setSuccess(true);
							l.setMessage("Event is in the past, not auto-assigning ... ");
						} else {
							boolean save = false;
							log.info(" ---> "+fe.getName()+": "+fe.getEventItems().size()+" items in event!");
							for(FmsEventItem fei : fe.getEventItems()) {
								log.info(" ---> "+fe.getName()+": "+fei.getMovieName());
								FmsEventAutoAssignLogItem li = new FmsEventAutoAssignLogItem();
								li.setMovieName(fei.getMovieName());
								li.setMovieId(fei.getMovieId());
								l.getItems().add(li);
								if(fei.getMovieVersionId()==null) {
									List<FmsMovieVersion> candidates = new ArrayList<>();
									for(FmsMovieVersion mv : versionService.listMovieVersions(null, fei.getMovieId(), null, null, null, null, null, null)) {
										if(!matches(mv,fei)) {
											log.info(" ---> "+fe.getName()+": "+fei.getMovieName()+" - ---> languages  don't match!");
										} else if(!playable(loc, mv)) {
											log.info(" ---> "+fe.getName()+": "+fei.getMovieName()+" - ---> not playable!");
										} else {
											log.info(" ---> "+fe.getName()+": "+fei.getMovieName()+" - one candidate!");
											candidates.add(mv);
										}
									}
									if(candidates.size()==1) {
										log.info(" ---> "+fe.getName()+": "+fei.getMovieName()+" - ---> auto assign possible!");
										li.setModified(true);
										fei.setMovieVersionId(candidates.get(0).getId());
										save = true;
									} 
									for(FmsMovieVersion c : candidates) {
										li.getCandidates().add(new FmsMovieVersionReference(c));
									}
								}
							}
							if(save) {
								eventService.save(fe);
							}
							l.setSuccess(true);
							l.setMessage("OK");
						}
						
					} catch (Exception e) {
						l.setSuccess(false);
						l.setMessage("error updating event: "+e.getMessage());
						log.error("error updating event", e);
					}
					autoAssign = save(autoAssign);
				}
				autoAssign.setEndTime(new Date());
				autoAssign = save(autoAssign);
			} catch (Exception e) {
				log.error("error setting stuff",e);
			} finally {
				FmsProjectHolder.clear();
				FmsSessionHolder.clear();
			}
		}
	}
	
	
	
	

}
