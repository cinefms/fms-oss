package com.openfms.core.service.project.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsCountryService;
import com.openfms.core.service.project.FmsCryptoKeyService;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsLanguageService;
import com.openfms.core.service.project.FmsLocationService;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.core.service.project.FmsSiteService;
import com.openfms.core.service.project.FmsTagService;
import com.openfms.core.service.project.ImportService;
import com.openfms.model.core.global.FmsCountry;
import com.openfms.model.core.global.FmsLanguage;
import com.openfms.model.core.global.FmsTag;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.core.movie.ImportResult;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsEventItem;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.core.playback.FmsSite;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.ConcurrencyException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;
import com.openfms.utils.common.md5.MD5;

@Service
public class ImportServiceImpl implements ImportService {
	
	private static Log log = LogFactory.getLog(ImportServiceImpl.class);

	@Autowired
	private FmsMovieService movieService;
	
	@Autowired
	private FmsMovieVersionService movieVersionService;
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	@Autowired
	private FmsMediaClipService mediaClipService;
	
	@Autowired
	private FmsCryptoKeyService keyService;
	
	@Autowired
	private FmsEventService eventService;

	@Autowired
	private FmsLocationService locationService;
	
	@Autowired
	private FmsSiteService siteService;
	
	@Autowired
	private FmsLanguageService languageService;
	
	@Autowired
	private FmsCountryService countryService;
	
	@Autowired
	private FmsTagService tagService;

	private final Lock  movieLock = new ReentrantLock();
	private final Lock  eventLock = new ReentrantLock();
	
	
	
	private List<String> checkCountryIds(List<String> countryIds) {
		List<String> out = new ArrayList<String>();
		for(String s : countryIds) {
			try {
				List<FmsCountry> fc = countryService.list(BasicQuery.createQuery().eq("code", s));
				fc.addAll(countryService.list(BasicQuery.createQuery().eq("codes", s)));
				if(fc.size()>0) {
					out.add(fc.get(0).getId());
				} else {
					throw new RuntimeException("unknown country id: "+s);
				}
			} catch (Exception e) {
				throw new RuntimeException("error looking up country id: "+s,e);
			}
		}
		return out;
	}

	private List<String> checkLanguageIds(List<String> languageIds) {
		List<String> out = new ArrayList<String>();
		for(String s : languageIds) {
			try {
				List<FmsLanguage> lc = languageService.list(BasicQuery.createQuery().eq("code", s));
				lc.addAll(languageService.list(BasicQuery.createQuery().eq("codes", s)));
				if(lc.size()>0) {
					out.add(lc.get(0).getId());
				} else {
					throw new RuntimeException("unknown language id: "+s);
				}
			} catch (Exception e) {
				throw new RuntimeException("error looking up language id: "+s,e);
			}
		}
		return out;
	}
	
	@Override
	public List<ImportResult> importMovies(List<FmsMovie> movies, boolean deleteObsolete) throws DatabaseException, EntityNotFoundException, AccessDeniedException, ConcurrencyException {

		if(!movieLock.tryLock()) {
			throw new ConcurrencyException("an import is already running", null);
		}
		
		try {
			
			List<FmsMovie> existingMovies = movieService.listMovies(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, null, null);
			Map<String,FmsMovie> emm = new HashMap<String, FmsMovie>();
			for(FmsMovie m : existingMovies) {
				emm.put(m.getExternalId(), m);
			}
			List<ImportResult> out = new ArrayList<ImportResult>();
			for(FmsMovie m : movies) {
				ImportResult ir = new ImportResult();
				ir.setName(m.getName());
				try {
					
					if(m.getExternalId()==null) {
						m.setExternalId(MD5.getHash(m.getName()));
					}
					FmsMovie mm = emm.remove(m.getExternalId());
					if(mm==null) {
						ir.setAction("ADDED");
						mm = new FmsMovie();
					} else {
						ir.setAction("UPDATED");
					}
					mm.setName(m.getName());
					mm.setLength(m.getLength());
					mm.setExternalId(m.getExternalId());
					mm.setDirector(m.getDirector());
					mm.setCategory(m.getCategory());
					mm.setContact(m.getContact());
					mm.setCountryIds(checkCountryIds(mm.getCountryIds()));
					movieService.save(mm);
					ir.setStatus("OK");
					
				} catch (Exception e2) {
					ir.setStatus("ERROR");
					ir.setMessage(e2.getMessage());
					log.error("error importing movie: "+m.getName(),e2);
				}
				out.add(ir);
			}
			if(deleteObsolete) {
				for(FmsMovie mmm : emm.values()) {
					ImportResult ir = new ImportResult();
					ir.setName(mmm.getName());
					ir.setAction("REMOVED");
					try {
						boolean keep = false;
						if(movieVersionService.listMovieVersions(null, mmm.getId(), null, null, null, null, null, null).size()>0) {
							keep = true;
						} else if(moviePackageService.listMoviePackages(null, mmm.getId(), null, null, null, null, null, null, true, null, null).size()>0) {
							keep = true;
						} else if(mediaClipService.list(BasicQuery.createQuery().eq("movieId", mmm.getId())).size()>0) {
							keep = true;
						} else if(keyService.listKeys(null, null, null, mmm.getId(), null, null, null, null, null, null, null, null, null, null, false, 0, 1).size()>0) {
							keep = true;
						} 
						if(!keep) {
							ir.setStatus("OK");
							movieService.delete(mmm.getId());
		 				} else {
		 					ir.setStatus("KEEP");
		 				}
					} catch (Exception e2) {
						ir.setStatus("ERROR");
						log.error("error removing movie: "+mmm.getName(),e2);
					}
					out.add(ir);
				}
			}
			return out;
		
		} catch (DatabaseException e1) {
			throw e1;
		} catch (EntityNotFoundException e2) {
			throw e2;
		} catch (AccessDeniedException e3) {
			throw e3;
		} finally {
			movieLock.unlock();
		}
		
		
	}

	@Override
	public List<ImportResult> importEvents(List<FmsEvent> events, boolean deleteObsolete) throws DatabaseException, EntityNotFoundException, InvalidParameterException, AccessDeniedException, ConcurrencyException {
		
		if(!eventLock.tryLock()) {
			throw new ConcurrencyException("an import is already running", null);
		}
		
		try {
			
			
			List<FmsEvent> existingEvents = eventService.listEvents(null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, null, null);
			Map<String,FmsEvent> eem = new HashMap<String, FmsEvent>();
			for(FmsEvent e : existingEvents) {
				eem.put(e.getExternalId(), e);
			}
	
			Map<String,FmsLocation> lmn = new HashMap<String, FmsLocation>();
			Map<String,FmsLocation> lmid = new HashMap<String, FmsLocation>();
			Map<String,FmsLocation> lmeid = new HashMap<String, FmsLocation>();
			for(FmsLocation l : locationService.listLocations(null, null, null, null, null, null, true, null, null)) {
				lmid.put(l.getId(), l);
				lmn.put(l.getName(), l);
				lmeid.put(l.getExternalId(), l);
			}
			
			List<ImportResult> out = new ArrayList<ImportResult>();
			for(FmsEvent e : events) {
				ImportResult ir = new ImportResult();
				ir.setName(e.getName());
				try {
					if(e.getExternalId()==null) {
						e.setExternalId(MD5.getHash(e.getName()));
					}
					FmsEvent ee = eem.remove(e.getExternalId());
					if(ee==null) {
						ir.setAction("ADDED");
						ee = new FmsEvent();
					} else {
						ir.setAction("UPDATED");
					}
					ee.setExternalId(e.getExternalId());
					ee.setName(e.getName());
					ee.setCategory(e.getCategory());
					ee.setStartTime(e.getStartTime());
					ee.setEndTime(e.getEndTime());
	
					FmsLocation loc = null;
					if(e.getExternalLocationId()!=null) {
						loc = lmeid.get(e.getExternalLocationId());
						if(loc == null) {
							throw new RuntimeException("unknown location with external id: "+e.getExternalId());
						}
					} else if(e.getLocationId()!=null) {
						loc = lmid.get(e.getLocationId());
						if(loc == null) {
							throw new RuntimeException("unknown location with id: "+e.getExternalId());
						}
					} else if(e.getLocationName()!=null) {
						if(lmn.get(e.getLocationName())!=null) {
							loc = lmn.get(e.getLocationName());
						} else {
							loc = new FmsLocation();
							loc.setName(e.getLocationName());
							loc = locationService.save(loc);
							lmn.put(loc.getName(), loc);
							lmid.put(loc.getId(), loc);
						}
					}
					
					if(loc!=null) {
						ee.setLocationId(loc.getId());
						ee.setLocationName(loc.getName());
					} else {
						throw new RuntimeException("missing location info");
					}
					
					List<FmsEventItem> newEventItems = new ArrayList<FmsEventItem>(e.getEventItems());
					List<FmsEventItem> oldEventItems = new ArrayList<FmsEventItem>(ee.getEventItems());
					
					e.getEventItems().clear();
					
					for(FmsEventItem fei1 : newEventItems) {
						FmsEventItem fei = null; 
						if(fei1.getExternalId()!=null) {
							for(FmsEventItem fei2 : oldEventItems) {
								if(fei2.getExternalId()!=null&&fei2.getExternalId().equals(fei1.getExternalId())) {
									fei = fei2;
									break;
								}
							}
						}
						if(fei==null) {
							for(FmsEventItem fei2 : oldEventItems) {
								if(fei2.getMovieExternalId().compareTo(fei1.getMovieExternalId())==0) {
									fei = fei2;
									break;
								}
							}
						}
						if(fei==null) {
							fei = new FmsEventItem();
							fei.setMovieExternalId(fei1.getMovieExternalId());
						}
						fei.setExternalId(fei1.getExternalId());
						
						List<FmsMovie> m = movieService.listMovies(null, fei.getMovieExternalId(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, null, null);
						if(m.size()<1) {
							throw new RuntimeException("unknown movie!");
						}
						if(m.size()>1) {
							throw new RuntimeException("non-unique movie! (expected one, got "+m.size()+" for external ID "+fei.getMovieExternalId()+")");
						}
						fei.setLength(fei1.getLength());
						fei.setMovieId(m.get(0).getId());
						fei.setAudioLanguageIds(checkLanguageIds(fei1.getAudioLanguageIds()));
						fei.setSubtitleLanguageIds(checkLanguageIds(fei1.getSubtitleLanguageIds()));
						fei.setName(fei1.getName());
						fei.setTags(null);
						for(String t : fei1.getTags()) {
							if(!fei.getTags().contains(t)) {
								FmsTag ft = null;
								try {
									ft = tagService.get(t);
								} catch (EntityNotFoundException ex) {
									ft = new FmsTag();
									ft.setName(t);
									tagService.save(ft);
								}
								fei.addTag(ft.getId());
							}
						}
						e.getEventItems().add(fei);
					}
					
					ee.setEventItems(e.getEventItems());
					
					eventService.save(ee);
					
					ir.setStatus("OK");
	
				} catch (Exception e2) {
					ir.setStatus("ERROR");
					ir.setMessage(e2.getMessage());
					log.error("error importing event: "+e.getName(),e2);
					
				}
				out.add(ir);
			}
			if(deleteObsolete) {
				for(FmsEvent eee : eem.values()) {
					ImportResult ir = new ImportResult();
					ir.setName(eee.getName());
					ir.setAction("REMOVED");
					try {
						eventService.delete(eee.getId());
						ir.setStatus("OK");
					} catch (Exception e2) {
						ir.setStatus("ERROR");
						ir.setMessage(e2.getMessage());
						log.error("error removing event: "+eee.getName(),e2);
					}
					out.add(ir);
				}
			}
			
			return out;
		} catch (DatabaseException e1) {
			throw e1;
		} catch (EntityNotFoundException e2) {
			throw e2;
		} catch (AccessDeniedException e3) {
			throw e3;
		} finally {
			eventLock.unlock();
		}
	}

	@Override
	public List<ImportResult> importSites(List<FmsSite> sites, boolean deleteObsolete) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		
		List<String> added = new ArrayList<String>();
		
		List<FmsSite> existingSites = siteService.listSites(null, null, null, true, null, null);

		Map<String,FmsSite> sByExtId = new HashMap<String, FmsSite>();
		Map<String,FmsSite> sByName = new HashMap<String, FmsSite>();
		for(FmsSite e : existingSites) {
			sByExtId.put(e.getExternalId(), e);
			sByName.put(e.getName(), e);
		}

		List<ImportResult> out = new ArrayList<ImportResult>();
		
		for(FmsSite site : sites) {
			ImportResult ir = new ImportResult();
			ir.setName(site.getName());
			try {
				boolean update = false;
				if(site.getExternalId()!=null) {
					FmsSite sExtId = sByExtId.get(site.getExternalId());
					if(sExtId!=null) {
						site.setId(sExtId.getId());
						update = true;
					}
				} 
				if(!update) {
					FmsSite sName = sByName.get(site.getName());
					if(sName!=null) {
						site.setId(sName.getId());
						update = true;
					}
					
				}
				ir.setAction(update?"UPDATED":"ADDED");
				site = siteService.save(site);
				added.add(site.getId());
				ir.setStatus("OK");
			} catch (Exception e) {
				ir.setStatus("ERROR");
				ir.setMessage(e.getMessage());
				log.error("error importing site: "+site.getName(),e);
			}
			out.add(ir);
		}
		
		if(deleteObsolete) {
			for(FmsSite s : existingSites) {
				if(!added.contains(s.getId())) {
					ImportResult ir = new ImportResult();
					ir.setName(s.getName());
					ir.setAction("REMOVED");
					try {
						siteService.delete(s.getId());
						ir.setStatus("OK");
					} catch (Exception e2) {
						ir.setStatus("ERROR");
						ir.setMessage(e2.getMessage());
						log.error("error removing site: "+s.getName(),e2);
					}
					out.add(ir);
				}
			}
		}
		return out;
	}

	@Override
	public List<ImportResult> importLocations(List<FmsLocation> locations, boolean deleteObsolete) throws DatabaseException, EntityNotFoundException, AccessDeniedException {

		List<String> added = new ArrayList<String>();

		Map<String,FmsLocation> lExtId = new HashMap<String, FmsLocation>();
		Map<String,FmsLocation> lName = new HashMap<String, FmsLocation>();
		List<FmsLocation> existingLocations = locationService.listLocations(null, null, null, null, null, null, true, null, null);
		for(FmsLocation l : existingLocations) {
			lExtId.put(l.getExternalId(), l);
			lName.put(l.getName(), l);
		}

		Map<String,FmsSite> sExtId = new HashMap<String, FmsSite>();
		List<FmsSite> existingSites = siteService.listSites(null, null, null, true, null, null);
		for(FmsSite e : existingSites) {
			sExtId.put(e.getExternalId(), e);
		}

		
		List<ImportResult> out = new ArrayList<ImportResult>();
		
		for(FmsLocation location : locations) {
			ImportResult ir = new ImportResult();
			ir.setName(location.getName());
			try {
				boolean update = false;
				if(location.getExternalId()!=null) {
					FmsLocation l = lExtId.get(location.getExternalId());
					if(l!=null) {
						location.setId(l.getId());
						update = true;
					}
				} 
				if(!update) {
					FmsLocation l = lExtId.get(location.getName());
					if(l!=null) {
						location.setId(l.getId());
						update = true;
					}
				}
				
				if(location.getExternalSiteId()!=null) {
					FmsSite s = sExtId.get(location.getExternalSiteId());
					if(s!=null) {
						location.setSiteId(s.getId());
					} else {
						
					}
				} 
				
				location = locationService.save(location);
				
				ir.setStatus(update?"UPDATED":"OK");
			} catch (Exception e) {
				ir.setStatus("ERROR");
				ir.setMessage(e.getMessage());
				log.error("error importing location: "+location.getName(),e);
			}
			out.add(ir);
		}
		
		if(deleteObsolete) {
			for(FmsLocation l : existingLocations) {
				if(!added.contains(l.getId())) {
					ImportResult ir = new ImportResult();
					ir.setName(l.getName());
					ir.setAction("REMOVED");
					try {
						siteService.delete(l.getId());
						ir.setStatus("OK");
					} catch (Exception e2) {
						ir.setStatus("ERROR");
						ir.setMessage(e2.getMessage());
						log.error("error removing site: "+l.getName(),e2);
					}
					out.add(ir);
				}
			}
		}
		return out;
	}

}
