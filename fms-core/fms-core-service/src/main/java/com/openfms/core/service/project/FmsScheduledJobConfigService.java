package com.openfms.core.service.project;

import java.util.List;

import com.cinefms.dbstore.query.api.exceptions.MalformedQueryException;
import com.openfms.core.service.GenericService;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobConfig;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobLog;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobLogBrief;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobType;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.VersioningException;

public interface FmsScheduledJobConfigService extends GenericService<FmsScheduledJobConfig> {

	public FmsScheduledJobType getScheduledJobType(String scheduledJobTypeId);
	
	public List<FmsScheduledJobType> listScheduledJobTypes(String searchTerm);
	
	public FmsScheduledJobLog addLog(String scheduledJobId, FmsScheduledJobLog log) throws DatabaseException, AccessDeniedException, VersioningException;

	public List<FmsScheduledJobLogBrief> getLogs(String scheduledJobId, int start, int max) throws DatabaseException, MalformedQueryException, AccessDeniedException;

	public FmsScheduledJobLogBrief getLog(String scheduledJobId, String logId) throws DatabaseException, MalformedQueryException, AccessDeniedException;

	public FmsScheduledJobLog getLogDetails(String scheduledJobId, String logId) throws DatabaseException, MalformedQueryException, AccessDeniedException;
	
}
