package com.openfms.core.service.project.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.admin.impl.ProjectServiceImpl;
import com.openfms.core.service.admin.impl.auth.FmsAdminUser;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.model.core.auth.FmsSession;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectHolder;
import com.openfms.model.core.references.FmsEventReference;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.VersioningException;
import com.openfms.model.utils.ListenerTrackerHolder;

@Service
@EnableScheduling
public class FmsMediaClipServiceImpl extends GenericProjectServiceImpl<FmsMediaClip> implements FmsMediaClipService {

	private static Log log = LogFactory.getLog(FmsMediaClipServiceImpl.class);

	@Autowired
	private ProjectServiceImpl projectServiceImpl;
	
	
	@Scheduled(fixedRate=180000)
	public void updateNextEventDate() throws AccessDeniedException, DatabaseException, VersioningException {
		
		log.info("updating next event date in media clips ... ");

		FmsUser user = new FmsAdminUser();
		user.setName("[SYSTEM] (scheduler)");
		user.setDisplayName("System User (Scheduled Job)");
		FmsSession session = new FmsSession();
		session.setUser(user);
		FmsSessionHolder.set(session);

		try {
			for(FmsProject p : projectServiceImpl.getProjects(null, null, null)) {

				log.info("updating next event date in media clips ... "+p.getName()+" ... ");

				FmsProjectHolder.set(p);

				for(FmsMediaClip fmc : list(BasicQuery.createQuery())) {
					
					Date nextEventDate = null;  
					String nextEventId = null;  
					
					for(FmsEventReference fer : fmc.getEvents()) {
						if(fer.getScreeningDate().after(new Date())) {
							if(nextEventDate==null || nextEventDate.after(fer.getScreeningDate())) {
								nextEventDate = fer.getScreeningDate();
								nextEventId = fer.getEventId();
							} 
						}
					}
					
					if((nextEventDate+"").compareTo(fmc.getNextEventDate()+"")!=0) {
						try {
							fmc.setNextEventDate(nextEventDate);
							fmc.setNextEventId(nextEventId);
							save(fmc,true,false);
						} catch (Exception e) {
							log.error("error updating next event date in media clip",e);
						}
					}
					
					ListenerTrackerHolder.clear();
					
				}

				log.info("updating next event date in media clips ... "+p.getName()+" ... DONE!");
				
			}
			log.info("updating next event date in media clips ... DONE!");
			
		} catch (Exception e) {
			log.error("error updating next event date in media clips",e);
		} finally {
			FmsSessionHolder.clear();
		}
		// TODO: handle exception
	}
	
	
}
