package com.openfms.core.service.project.impl.listeners;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.Status;
import com.openfms.model.core.crypto.FmsCertificate;
import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.crypto.FmsKeyRequest;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.core.movie.FmsMovieVersion;
import com.openfms.model.core.playback.FmsError;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsEventItem;
import com.openfms.model.core.playback.FmsEventPBS;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.core.playback.MediaClipPlaybackStatus;
import com.openfms.model.core.references.FmsMediaClipReference;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.utils.StatusCombine;
import com.openfms.utils.common.text.StringUtil;


@Component
public class EventListener extends FmsListenerAdapter<FmsEvent> {
	
	private static Log log = LogFactory.getLog(EventListener.class);
	
	@Autowired
	private FmsMovieService movieService;
	
	@Autowired
	private FmsMovieVersionService movieVersionService;

	@Autowired
	private ProjectDataStore dataStore;
	
	public EventListener() {
	}

	private void updateLength(FmsEvent object) {
		try {
			if(object.getLength()>0) {
				object.setEndTime(DateUtils.addMinutes(object.getStartTime(),object.getLength()));
			} else if(object.getEndTime()!=null) {
				long length = object.getEndTime().getTime() - object.getStartTime().getTime();
				object.setLength((int)Math.round(length/60000));
			}
			
		} catch (Exception e) {
			throw new RuntimeException("error setting location name and device references", e);
		}
	}

	public Date maxBefore(Date d) {
		return DateUtils.truncate(DateUtils.addHours(d, -24), Calendar.DAY_OF_MONTH);
	}
	public Date maxAfter(Date d) {
		return DateUtils.addHours(d, 6);
	}
	public Date minBefore(Date d) {
		return DateUtils.addMinutes(d, -178);
	}
	public Date minAfter(Date d) {
		return DateUtils.addMinutes(d, 178);
	}
	
	private List<FmsPlaybackDevice> getDevices(String locationId, String mediaType) throws DatabaseException {
		DBStoreQuery q = BasicQuery.createQuery().eq("locationId", locationId).eq("mediaClipTypes", mediaType).eq("disabled", false);
		return dataStore.findObjects(FmsPlaybackDevice.class, q);
	}
	
	private void updateLocation(FmsEvent object) {
		try {
			FmsLocation loc = dataStore.getObject(FmsLocation.class, object.getLocationId());
			if(loc!=null) {
				object.setDevices(loc.getDevices());
				object.setLocationName(loc.getName());
			}			
		} catch (Exception e) {
			throw new RuntimeException("Error updating location of event" ,e);
		}
	}
	
	private void updateMovie(FmsEvent object) {
		try {
			
			for(FmsEventItem fei : object.getEventItems()) {
				FmsMovie movie = dataStore.getObject(FmsMovie.class, fei.getMovieId());
				fei.setMovieName(movie.getName());
				fei.setMovieExternalId(movie.getExternalId());
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Error updating movies" ,e);
		}
		

	}

	private void checkPlaybackStatus(FmsEvent object) {
		
		object.setPlaybackStatus(Status.NOT_APPLICABLE.value());
		for(FmsEventItem fei : object.getEventItems()) {
			fei.setPlaybackStatus(Status.NOT_APPLICABLE.value());
		}

		if(object.getId()==null) {
			return;
		}
		
		
		DBStoreQuery q = BasicQuery.createQuery();
		q = q.eq("eventId", object.getId());
		q = q.order("date",false);
		q = q.gte("date",new Date(System.currentTimeMillis()-3600000));
		q = q.start(0);
		q = q.max(1);
		
		try {
			List<FmsEventPBS> pbs = dataStore.findObjects(FmsEventPBS.class, q);

			int all = Status.NOT_APPLICABLE.value();
			
			if(pbs.size()>0) {
				for(FmsEventItem fei : object.getEventItems()) {
					int one = Status.NOT_APPLICABLE.value();
					for(FmsMediaClipReference mcr : fei.getMediaClips()) {
						if(mcr.getMediaClipExternalId()==null) {
							// not mediaclips
							continue;
						}
						List<MediaClipPlaybackStatus> ss = new ArrayList<MediaClipPlaybackStatus>();
						for(MediaClipPlaybackStatus mcs : pbs.get(0).getMediaClipStatus()) {
							if(mcs.getMediaClipExternalId()!=null && mcs.getMediaClipExternalId().compareTo(mcr.getMediaClipExternalId())==0) {
								ss.add(mcs);
							}
						}
						for(MediaClipPlaybackStatus mcs : ss) {
							one = StatusCombine.combine(one,mcs.getStatus());
							if(mcs.getMessage()!=null) {
								object.addError(mcs.getStatus(), mcs.getMessage());
							}
						}
					}
					fei.setPlaybackStatus(one);
					all = StatusCombine.combine(all,one);
				}
			}
			object.setPlaybackStatus(all);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error",e);
		}
	}

	private void checkVersionStatus(FmsEvent object) {
		try {
			
			int vsAll = Status.NOT_APPLICABLE.value();
			int implicitLength = 0;

			for(FmsEventItem fei : object.getEventItems()) {
				log.info("checking event item ... ");
				int vs = Status.NOT_APPLICABLE.value();
				implicitLength = implicitLength + fei.getLength();
				if(fei.getMovieVersionId()!=null) {
					
					FmsMovieVersion mv = dataStore.getObject(FmsMovieVersion.class, fei.getMovieVersionId());
					
					if(mv==null || mv.getMovieId().compareTo(fei.getMovieId())!=0) {
						fei.setMovieVersionId(null);
						fei.setMovieVersionName(null);
					} else if(mv.isDisabled() && object.getStartTime().after(new Date())) {
						fei.setMovieVersionId(null);
						fei.setMovieVersionName(null);
					} else {

						fei.setMovieVersionName(mv.getDisplayName());
						if(fei.getName()==null || fei.getName().trim().length()==0) {
							fei.setName(mv.getName());
						}

						vs = Status.OK.value();

						if(mv.getSignature().compareTo(fei.getSignature())!=0) {
							if(log.isDebugEnabled()) { 
								log.debug("EVENT: version status / signature mismatch"); 
							}
							object.addError(Status.WARNING.value(),"Language mismatch for movie version "+mv.getName()+": "+mv.getSignature()+" != "+fei.getSignature());
							vs = StatusCombine.combine(vs,Status.WARNING.value());
						}
						if(Math.abs(mv.getLength()-fei.getLength())>5) {
							if(log.isDebugEnabled()){ log.debug("EVENT: version status / length mismatch: "+mv.getLength()+" vs "+fei.getLength()); }
							object.addError(Status.WARNING.value(),"Length mismatch for movie version "+mv.getName()+" (scheduled: "+fei.getLength()+" minutes, but movie version is "+mv.getLength()+").");
							vs = StatusCombine.combine(vs,Status.WARNING.value());
						}
						
						// check version tags
						for(String t : fei.getTags()) {
							for(FmsMediaClipReference r : fei.getMediaClips()) {
								if(!r.getTags().contains(t)) {
									object.addError(Status.WARNING.value(),"Media clip tag mismatch for movie version "+mv.getName()+".");
									vs = StatusCombine.combine(vs,Status.WARNING.value());
									break;
								}
							}
						}

						// check device capabilities & tags
						for(FmsMediaClipReference r : fei.getMediaClips()) {
							List<FmsPlaybackDevice> devices = getDevices(object.getLocationId(),r.getMediaClipType());
							if(devices.size()==0) {
								object.addError(Status.ERROR.value(),"No playback device found that supports the media type "+r.getMediaClipType()+".");
								vs = StatusCombine.combine(vs,Status.ERROR.value());
							}
							for(FmsPlaybackDevice pd : devices) {
								for(String t : fei.getTags()) {
									if(!pd.getTags().contains(t)) {
										object.addError(Status.WARNING.value(),"Playback device tag mismatch for device "+pd.getDisplayName()+".");
										vs = StatusCombine.combine(vs,Status.WARNING.value());
										break;
									}
								}
							}
						}
					}
					
				} else {
           			vs = Status.PENDING.value();
				}
				fei.setVersionStatus(vs);
				vsAll = StatusCombine.combine(vs,vsAll);
			}
			object.setVersionStatus(vsAll);
			object.setImplicitLength(implicitLength);
		} catch (Exception e) {
			throw new RuntimeException("Error checking version status" ,e);
		}
	}
		
	private void checkMediaStatus(FmsEvent object) {
		try {
			int msAll = Status.NOT_APPLICABLE.value();

			for(FmsEventItem fei : object.getEventItems()) {

				List<FmsMediaClipReference> refs = new ArrayList<FmsMediaClipReference>();

				int ms = Status.NOT_APPLICABLE.value();
				
				if(fei.getMovieVersionId()!=null) {
					FmsMovieVersion mv = dataStore.getObject(FmsMovieVersion.class, fei.getMovieVersionId());
					if(mv.getMediaClipIds()==null || mv.getMediaClipIds().size()==0) {
						ms = StatusCombine.combine(ms,Status.WARNING.value());
						object.addError(Status.WARNING.value(),"Movie Version "+mv.getName()+" has no media clips.");
					}
					for(String mcId : mv.getMediaClipIds()) {
						FmsMediaClip mc = dataStore.getObject(FmsMediaClip.class, mcId);
						if(mc.isDisabled()) {
							object.addError(Status.ERROR.value(),"Media Clip "+mc.getName()+" is disabled.");
							ms = Status.ERROR.value();
						} else if(mc.getSignature().compareTo(fei.getSignature())!=0) {
							object.addError(Status.WARNING.value(),"Language mismatch on Media Clip "+mc.getName()+": "+mc.getSignature()+" != "+fei.getSignature());
							ms = Status.WARNING.value();
						}
						
						
						if(mc.getStatus()==Status.PENDING.value()) {
							object.addError(mc.getStatus(),"QC on Media Clip "+mc.getName()+" is not complete.");
						} else if(mc.getStatus()==Status.WARNING.value()) {
							object.addError(mc.getStatus(),"Media Clip "+mc.getName()+" has a warning");
						} else if(mc.getStatus()==Status.ERROR.value()) {
							object.addError(mc.getStatus(),"Media Clip "+mc.getName()+" has an error!");
						} 
						ms = StatusCombine.combine(ms,mc.getStatus());
						if(mc!=null) {
							refs.add(new FmsMediaClipReference(mc));
						}
					}
				}
				msAll = StatusCombine.combine(ms,msAll);
				fei.setMediaStatus(ms);
				fei.setMediaClips(refs);
			}
			object.setMediaStatus(msAll);
			
		} catch (Exception e) {
			throw new RuntimeException("Error checking media status" ,e);
		}
	}

	private void checkEncryptionStatus(FmsEvent object) {
		try {
			int esAll = Status.NOT_APPLICABLE.value();
			for(FmsEventItem fei : object.getEventItems()) {
				int es = Status.PENDING.value();
				if(fei.getMovieVersionId()!=null) {
					Set<String> keyIds = new TreeSet<String>();
					FmsMovieVersion mv = dataStore.getObject(FmsMovieVersion.class, fei.getMovieVersionId());
					if(mv.getMediaClipIds()!=null && mv.getMediaClipIds().size()>0) {
						es = Status.NOT_APPLICABLE.value();
						for(String mcId : mv.getMediaClipIds()) {
							FmsMediaClip mc = dataStore.getObject(FmsMediaClip.class, mcId);
							List<FmsPlaybackDevice> devices = getDevices(object.getLocationId(), mc.getType());
							if(mc.isEncrypted()) {
								if(devices.size()==0) {
									object.addError(Status.ERROR.value(),"No matching devices configured for the location, cannot check certificate");
									es = StatusCombine.combine(es,Status.ERROR.value());
								}
								for(FmsPlaybackDevice pd : devices) {
									if(pd.getCertificateId()!=null) {
										
										FmsCertificate c = dataStore.getObject(FmsCertificate.class, pd.getCertificateId());
										
										DBStoreQuery q = BasicQuery.createQuery();
										q = q.eq("mediaClipExternalId", mc.getExternalId());
										q = q.eq("certificateId", pd.getCertificateId());
										
										List<FmsKey> keysMax = new ArrayList<FmsKey>();
										{
											DBStoreQuery qMax = q.lte("validFrom", maxBefore(object.getStartTime()));
											qMax = qMax.gte("validTo", maxAfter(object.getStartTime()));
	
											keysMax = dataStore.findObjects(FmsKey.class, qMax);
										}
										
										List<FmsKey> keysMin = new ArrayList<FmsKey>();
										{
											DBStoreQuery qMin = q.lte("validFrom", minBefore(object.getStartTime()));
											qMin = qMin.gte("validTo", minAfter(object.getStartTime()));
											keysMin = dataStore.findObjects(FmsKey.class, qMin);
										}

										List<FmsKey> keysAtAll = new ArrayList<FmsKey>();
										{
											DBStoreQuery qAtAll = q.lte("validFrom", object.getStartTime());
											qAtAll = qAtAll.gte("validTo", object.getStartTime());
											keysAtAll = dataStore.findObjects(FmsKey.class, qAtAll);
										}
										
										for(FmsKey k : keysMax) {
											if(!c.matchesTDL(k)) {
												object.addError(Status.WARNING.value(), "Key "+k.getId()+" does not match the required list of TDL thumbprints.");
											} else {
												keyIds.add(k.getId()); 
											} 
										}
										for(FmsKey k : keysMin) {
											if(!c.matchesTDL(k)) {
												object.addError(Status.WARNING.value(), "Key "+k.getId()+" does not match the required list of TDL thumbprints.");
											} else {
												keyIds.add(k.getId()); 
											} 
										}
										for(FmsKey k : keysAtAll) {
											if(!c.matchesTDL(k)) {
												object.addError(Status.WARNING.value(), "Key "+k.getId()+" does not match the required list of TDL thumbprints.");
											} else {
												keyIds.add(k.getId()); 
											}
										}
											
										if(keysMax.size()>0) {
											es = StatusCombine.combine(es,Status.OK.value());
										} else if(keysMin.size()>0) {
											object.addError(Status.WARNING.value(),"Available keys for media clip "+mc.getName()+" are a bit tight.");
											es = StatusCombine.combine(es,Status.WARNING.value());
										} else if (keysAtAll.size()>0) {
											for(FmsKey k : keysAtAll) {
												long before = (object.getStartTime().getTime()-k.getValidFrom().getTime())/60000;
												long after = (k.getValidTo().getTime()-object.getStartTime().getTime())/60000;
												object.addError(Status.WARNING.value(),"Key "+k.getId()+" is too tight ("+before+" minutes before, "+after+" minutes after).");
											}
											es = StatusCombine.combine(es,Status.WARNING.value());
										} else {
											object.addError(Status.ERROR.value(),"No matching keys available keys for media clip "+mc.getName()+".");
											es = StatusCombine.combine(es,Status.ERROR.value());
										}
									} else {
										object.addError(Status.ERROR.value(),"No certificate configured for device: "+pd.getDisplayName());
										es = StatusCombine.combine(es,Status.ERROR.value());
									}
								}
							}
						}
					}
					fei.setKeyIds(new ArrayList<String>(keyIds));
				}
				
				fei.setEncryptionStatus(es);
				
				esAll = StatusCombine.combine(es,esAll);
				
			}
			
			object.setEncryptionStatus(esAll);
		} catch (Exception e) {
			throw new RuntimeException("Error checking encryption status" ,e);
		}
	}

	
	
	private void updateKeyRequests(FmsEvent n) {
		try {
			
			FmsLocation loc = dataStore.getObject(FmsLocation.class, n.getLocationId());
			List<FmsPlaybackDevice> devices = dataStore.findObjects(FmsPlaybackDevice.class, BasicQuery.createQuery().eq("locationId", n.getLocationId()));
			for(FmsEventItem fei : n.getEventItems()) {
				if(fei.getMovieVersionId()!=null && fei.getEncryptionStatus()!=Status.OK.value()) {
					FmsMovieVersion mv = dataStore.getObject(FmsMovieVersion.class, fei.getMovieVersionId());
					for(String mcId : mv.getMediaClipIds()) {
						FmsMediaClip mc = dataStore.getObject(FmsMediaClip.class, mcId);
						if(mc.isEncrypted()) {
							for(FmsPlaybackDevice d : devices) {
								if(d.isDisabled()) {
									// ignore
								} else if(d.getMediaClipTypes().contains(mc.getType()) && d.getCertificateId()!=null) {
									FmsKeyRequest req = new FmsKeyRequest();
									req.setMovieId(fei.getMovieId());
									req.setDeviceId(d.getId());
									req.setMediaClipId(mc.getId());
									req.setMediaClipExternalId(mc.getExternalId());
									req.setLocationId(loc.getId());
									req.setStart(maxBefore(n.getStartTime()));
									req.setEnd(maxAfter(n.getEndTime()));
									dataStore.saveObject(req, true, false);
								}
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Error updating key requests ",e);
		}
	}

	@Override
	protected boolean beforeSave(String db, FmsEvent object) {
		object.setErrors(new ArrayList<FmsError>());
		
		updateLocation(object);
		updateLength(object);
		updateMovie(object);
		checkMediaStatus(object);
		checkVersionStatus(object);
		updateKeyRequests(object);
		checkEncryptionStatus(object);
		try {
			checkPlaybackStatus(object);
		} catch (Exception e) {
			object.setPlaybackStatus(Status.ERROR.value());
			object.addError(Status.ERROR.value(), "Failed to check playback status because: "+StringUtil.getStackTrace(e));
		}
		return true;
	}

	@Override
	protected void beforeDelete(String db, FmsEvent object) {
	}

	@Override
	protected void created(String db, FmsEvent object) {
		updated(db,null,object);
	}

	@Override
	protected void deleted(String db, FmsEvent object) {
		updated(db,object,null);
	}

	@Override
	protected void updated(String db, FmsEvent o, FmsEvent n) {
		try {
			Set<String> movieIds = new TreeSet<String>();
			Set<String> movieVersionIds = new TreeSet<String>();
			for(FmsEvent e : new FmsEvent[] { o, n }) {
				if(e!=null) {
					for(FmsEventItem fei : e.getEventItems()) {
						if(fei.getMovieId()!=null) {
							movieIds.add(fei.getMovieId());
						}
						if(fei.getMovieVersionId()!=null) {
							movieVersionIds.add(fei.getMovieVersionId());
						}
					}
				}
			}
			for(String movieId : movieIds) {
				movieService.update(movieId);
			}
			for(String movieVersionId : movieVersionIds) {
				movieVersionService.update(movieVersionId);
			}

		} catch (Exception e) {
			throw new RuntimeException("error updating dependencies",e);
		}
	}

	

}
