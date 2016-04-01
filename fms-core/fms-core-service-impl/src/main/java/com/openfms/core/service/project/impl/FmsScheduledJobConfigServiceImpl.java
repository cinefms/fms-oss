package com.openfms.core.service.project.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.exceptions.MalformedQueryException;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.project.FmsScheduledJobConfigService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobConfig;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobFactory;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobLog;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobLogBrief;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobType;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.VersioningException;

@Service
public class FmsScheduledJobConfigServiceImpl extends GenericProjectServiceImpl<FmsScheduledJobConfig> implements FmsScheduledJobConfigService {

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private ProjectDataStore dataStore;

	@Autowired
	private AuthzService authzService;

	@Override
	public List<FmsScheduledJobType> listScheduledJobTypes(String searchTerm) {
		List<FmsScheduledJobFactory> scheduledJobFactories = new ArrayList<FmsScheduledJobFactory>(ctx.getBeansOfType(FmsScheduledJobFactory.class).values());
		List<FmsScheduledJobType> out = new ArrayList<FmsScheduledJobType>();
		for (FmsScheduledJobFactory f : scheduledJobFactories) {
			for (FmsScheduledJobType jt : f.getTypes()) {
				if (searchTerm == null || jt.getName().toUpperCase().contains(searchTerm.toUpperCase())) {
					out.add(jt);
				}
			}
		}
		return out;
	}

	@Override
	public FmsScheduledJobType getScheduledJobType(String scheduledJobTypeId) {
		for (FmsScheduledJobType jt : listScheduledJobTypes(null)) {
			if (jt.getId().compareTo(scheduledJobTypeId) == 0) {
				return jt;
			}
		}
		return null;
	}

	@Override
	public FmsScheduledJobLog addLog(String scheduledJobId, FmsScheduledJobLog log) throws DatabaseException, AccessDeniedException, VersioningException {
		FmsScheduledJobConfig sjc = dataStore.getObject(FmsScheduledJobConfig.class, scheduledJobId);
		if (sjc.getUserIds().contains(FmsSessionHolder.getCurrentUser().getId())) {
			log.setScheduledJobId(scheduledJobId);
			return dataStore.saveObject(log);
		}
		throw new AccessDeniedException();
	}

	@Override
	public List<FmsScheduledJobLogBrief> getLogs(String scheduledJobId, int start, int max) throws DatabaseException, MalformedQueryException, AccessDeniedException {
		if(!authzService.allowAccess(FmsScheduledJobConfig.class, scheduledJobId, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		DBStoreQuery q = BasicQuery.createQuery();
		q = q.eq("scheduledJobId", scheduledJobId);
		q = q.order("created",false);
		q = q.start(start);
		q = q.max(max);
		
		List<FmsScheduledJobLogBrief> out = new ArrayList<FmsScheduledJobLogBrief>();
		for(FmsScheduledJobLog j : dataStore.findObjects(FmsScheduledJobLog.class, q)) {
			out.add(j.getBrief());
		}
		return out;
	}

	@Override
	public FmsScheduledJobLogBrief getLog(String scheduledJobId, String logId) throws DatabaseException, MalformedQueryException, AccessDeniedException {
		return getLogDetails(scheduledJobId, logId).getBrief();
	}
	
	
	@Override
	public FmsScheduledJobLog getLogDetails(String scheduledJobId, String logId) throws DatabaseException, MalformedQueryException, AccessDeniedException {
		if(!authzService.allowAccess(FmsScheduledJobConfig.class, scheduledJobId, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		DBStoreQuery q = BasicQuery.createQuery();
		q = q.eq("scheduledJobId", scheduledJobId);
		q = q.eq("_id", logId);
		
		return dataStore.findObject(FmsScheduledJobLog.class, q);
	}
	

}
