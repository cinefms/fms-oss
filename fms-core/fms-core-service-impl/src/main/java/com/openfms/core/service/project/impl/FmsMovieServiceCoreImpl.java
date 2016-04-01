package com.openfms.core.service.project.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.admin.impl.ProjectServiceImpl;
import com.openfms.core.service.admin.impl.auth.FmsAdminUser;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.model.core.auth.FmsSession;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectHolder;
import com.openfms.model.core.references.FmsEventReference;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;
import com.openfms.model.utils.ListenerTrackerHolder;

@Component
@EnableScheduling
public class FmsMovieServiceCoreImpl extends GenericProjectServiceImpl<FmsMovie> implements FmsMovieService {

	@Autowired
	private ProjectServiceImpl projectServiceImpl;
	
	
	@Override
	public List<FmsMovie> listMovies(List<String> ids, String movieExternalId, String category, Integer[] mediaStatus, Integer[] versionStatus, Integer[] encryptionStatus, Integer[] screeningStatus, Date dateFrom, Date dateTo, String categorySearch, Boolean openTasks, Boolean hasEvents, Boolean hasVersion, Boolean hasMediaClips, String searchTerm, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (searchTerm != null) {
			q = q.contains("searchable", searchTerm);
		}
		if (category != null) {
			q = q.eq("category", category);
		}
		if (ids != null) {
			q = q.in("id", ids);
		}
		if (movieExternalId != null) {
			q = q.eq("externalId", movieExternalId);
		}
		if (mediaStatus != null && mediaStatus.length>0) {
			q = q.in("mediaStatus", mediaStatus);
		}
		if (versionStatus != null && versionStatus.length>0) {
			q = q.in("versionStatus", versionStatus);
		}
		if (encryptionStatus != null && encryptionStatus.length>0) {
			q = q.in("encryptionStatus", encryptionStatus);
		}
		if (screeningStatus != null && screeningStatus.length>0) {
			q = q.in("screeningStatus", screeningStatus);
		}
		if(categorySearch!=null) {
			q = q.contains("category", categorySearch);
		}
		if(dateFrom!=null) {
			q = q.gte("firstEventDate", dateFrom);
		}
		if(dateTo!=null) {
			q = q.lte("firstEventDate", dateTo);
		}
		if(openTasks!=null) {
			if(openTasks.booleanValue()) {
				q = q.gte("openTasks", 1);
			} else {
				q = q.eq("openTasks", 0);
			}
		}
		if(hasEvents!=null) {
			if(hasEvents.booleanValue()) {
				q = q.gte("numEvents", 1);
			} else {
				q = q.eq("numEvents", 0);
			}
		}
		if(hasVersion!=null) {
			if(hasVersion.booleanValue()) {
				q = q.gte("numVersion", 1);
			} else {
				q = q.eq("numVersion", 0);
			}
		}
		if(hasMediaClips!=null) {
			if(hasMediaClips.booleanValue()) {
				q = q.gte("numClips", 1);
			} else {
				q = q.eq("numClips", 0);
			}
		}
		if (order != null) {
			q = q.order(order, asc);
		} else {
			q = q.order("name");
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		return list(q);
	}

	private static Log log = LogFactory.getLog(FmsMediaClipServiceImpl.class);
	
	@Scheduled(fixedRate=180000)
	public void updateNextEventDate() throws AccessDeniedException, DatabaseException, VersioningException {

		log.info("updating next event date in movies ... ");

		FmsUser user = new FmsAdminUser();
		user.setName("[SYSTEM] (scheduler)");
		user.setDisplayName("System User (Scheduled Job)");
		FmsSession session = new FmsSession();
		session.setUser(user);
		FmsSessionHolder.set(session);

		try {
			
			for(FmsProject p : projectServiceImpl.getProjects(null, null, null)) {

				log.info("updating next event date in movies ... "+p.getName()+" ... ");
			
				FmsProjectHolder.set(p);
			
				for(FmsMovie fm : list(BasicQuery.createQuery())) {
					Date nextEventDate = null;  
					String nextEventId = null;  
					
					for(FmsEventReference fer : fm.getEvents()) {
						if(fer.getScreeningDate().after(new Date())) {
							if(nextEventDate==null || nextEventDate.after(fer.getScreeningDate())) {
								nextEventDate = fer.getScreeningDate();
								nextEventId = fer.getEventId();
							} 
						}
					}
					
					if((nextEventDate+"").compareTo(fm.getNextEventDate()+"")!=0) {
						try {
							log.info("changed: "+fm.getId()+" / "+fm.getName());
							fm.setNextEventDate(nextEventDate);
							fm.setNextEventId(nextEventId);
							fm = save(fm,true,false);
							log.info(" - new next event date is: "+fm.getNextEventDate());
						} catch (Exception e) {
							log.error("error updating next event date in movie ",e);
						}
					}
					
					ListenerTrackerHolder.clear();

				}
				
				log.info("updating next event date in movies ... "+p.getName()+" ... DONE!");
				
				
			}

			log.info("updating next event date in movies ... DONE!");

		} catch (Exception e) {
			log.error("error updating next event date in movies ",e);
		} finally {
			FmsSessionHolder.clear();
		}
		
	}

	

}
