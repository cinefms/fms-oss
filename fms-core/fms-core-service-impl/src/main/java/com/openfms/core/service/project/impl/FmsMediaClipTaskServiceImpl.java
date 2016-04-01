package com.openfms.core.service.project.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.admin.impl.ProjectServiceImpl;
import com.openfms.core.service.admin.impl.auth.FmsAdminUser;
import com.openfms.core.service.project.FmsMediaClipTaskService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsSession;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.movie.FmsMediaClipTask;
import com.openfms.model.core.movie.FmsMediaClipTaskProgress;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectHolder;
import com.openfms.model.core.references.FmsEventReference;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

@Service
public class FmsMediaClipTaskServiceImpl extends GenericProjectServiceImpl<FmsMediaClipTask> implements FmsMediaClipTaskService {


	private static Log log = LogFactory.getLog(FmsMediaClipTaskServiceImpl.class);

	@Autowired
	private ProjectServiceImpl projectServiceImpl;

	@Override
	public List<FmsMediaClipTask> listMediaClipTasks(String mediaClipId, String userId, String projectUserId, String[] types, Integer[] status, Date usedAfter, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if(mediaClipId!=null) {
			q = q.eq("mediaClipId", mediaClipId);
		}
		if(userId!=null) {
			q = q.eq("userId", userId);
		}
		if(types!=null && types.length>0) {
			q = q.in("type", types);
		}
		if(status!=null && status.length>0) {
			q = q.in("status", status);
		}
		if(order!=null) {
			q = q.order(order,asc);
		}
		if(max!=null) {
			q = q.max(max);
		}
		if(start!=null) {
			q = q.start(start);
		}
		return list(q);
	}
	
	@Override
	public FmsMediaClipTaskProgress addProgress(FmsMediaClipTaskProgress progress) throws AccessDeniedException, DatabaseException, EntityNotFoundException, VersioningException {
		if(!authzService.allowAccess(FmsMediaClipTask.class,progress.getMediaClipTaskId(),AccessType.UPDATE)) {
			throw new AccessDeniedException();
		}
		return dataStore.saveObject(progress);
	}
	
	
	@Override
	public List<FmsMediaClipTaskProgress> getProgress(String mediaClipTaskId) throws AccessDeniedException, DatabaseException {
		if(!authzService.allowAccess(FmsMediaClipTask.class,mediaClipTaskId,AccessType.UPDATE)) {
			throw new AccessDeniedException();
		}
		DBStoreQuery q = BasicQuery.createQuery();
		q = q.eq("mediaClipTaskId", mediaClipTaskId);
		q = q.order("date",true);
		return dataStore.findObjects(FmsMediaClipTaskProgress.class, q);
	}

	
	@Scheduled(fixedRate=180000)
	public void updateNextEventDate() throws AccessDeniedException, DatabaseException, VersioningException {
		
		log.info("updating next event date in media clip tasks ... ");

		FmsUser user = new FmsAdminUser();
		user.setName("[SYSTEM] (scheduler)");
		user.setDisplayName("System User (Scheduled Job)");
		FmsSession session = new FmsSession();
		session.setUser(user);
		FmsSessionHolder.set(session);

		try {
			for(FmsProject p : projectServiceImpl.getProjects(null, null, null)) {
				
				log.info("updating next event date in media clip tasks ... "+p.getName()+" ... ");

				FmsProjectHolder.set(p);

				for(FmsMediaClipTask fmct : list(BasicQuery.createQuery())) {
					try {
						Date nextEventDate = fmct.getNextEventDate();
						String nextEventId = fmct.getNextEventId();
						Date newNextEventDate = null;
						String newNextEventId = null;
						for(FmsEventReference fer : fmct.getEvents()) {
							if(fer.getScreeningDate().getTime() < System.currentTimeMillis()) {
								// in the past
							} else if(newNextEventDate==null || newNextEventDate.after(fer.getScreeningDate())) {
								newNextEventDate = fer.getScreeningDate();
								newNextEventId = fer.getEventId();
							}
						}
						if((newNextEventDate+"").compareTo(nextEventDate+"")!=0 ||  (newNextEventId+"").compareTo(nextEventId+"")!=0) {
							fmct.setNextEventDate(newNextEventDate);
							fmct.setNextEventId(newNextEventId);
							save(fmct);
						}
						
					} catch (Exception e) {
						log.error("error updating next event date in media clip  task",e);
					}
				}

				log.info("updating next event date in media clip tasks ... "+p.getName()+" ... DONE!");
				
			}
			log.info("updating next event date in media clip tasks ... DONE!");
			
		} catch (Exception e) {
			log.error("error updating next event date in media clip tasks",e);
		} finally {
			FmsSessionHolder.clear();
		}
	}
	
	
	
}
