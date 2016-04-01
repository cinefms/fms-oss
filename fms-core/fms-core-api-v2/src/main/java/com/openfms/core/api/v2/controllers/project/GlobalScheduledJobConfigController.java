package com.openfms.core.api.v2.controllers.project;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsScheduledJobConfigService;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobConfig;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobLog;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobLogBrief;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(GlobalScheduledJobConfigController.BASE_URI)
public class GlobalScheduledJobConfigController extends GenericController<FmsScheduledJobConfig> {

	public static final String BASE_URI = "/global/scheduledjobs";
	
	@Autowired
	private FmsScheduledJobConfigService scheduledJobConfigService;
	
	@Override
	protected GenericService<FmsScheduledJobConfig> getService() {
		return scheduledJobConfigService;
	}
	
	@Override
	protected Class<FmsScheduledJobConfig> getType() {
		return FmsScheduledJobConfig.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsScheduledJobConfig> list(
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false,defaultValue="true") boolean asc, 
			@RequestParam(required=false) String userId, 
			@RequestParam(required=false) String userName, 
			@RequestParam(required=false) String order, 
			@RequestParam(required=false) Boolean active, 
			@RequestParam(required=false) Integer start, 
			@RequestParam(required=false,defaultValue="25") int max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = getBasicQuery(searchTerm, asc, order, start, max);
		if(userId!=null) {
			if(userId.compareTo("me")==0) {
				userId = FmsSessionHolder.getCurrentUser().getId();
			}
			q = q.in("userIds", userId);
		}
		if(userName!=null) {
			q = q.in("users.userName", userName);
		}
		if(active!=null) {
			q = q.in("active", active.booleanValue());
		}
		return getService().list(q);
	}
	

	@RequestMapping(value="/{scheduledJobId}/logs",method=RequestMethod.POST)
	@ResponseBody
	public FmsScheduledJobLog addScheduledJobLog(@PathVariable String scheduledJobId, @Valid @RequestBody FmsScheduledJobLog log) throws DatabaseException, EntityNotFoundException, AccessDeniedException, VersioningException {
		log.setScheduledJobId(scheduledJobId);
		return scheduledJobConfigService.addLog(scheduledJobId,log);
	}
	
	@RequestMapping(value="/{scheduledJobId}/logs",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsScheduledJobLogBrief> listScheduledJobLogs(
			@PathVariable String scheduledJobId,
			@RequestParam(defaultValue="0",required=false) int start,
			@RequestParam(defaultValue="100",required=false) int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return scheduledJobConfigService.getLogs(scheduledJobId,start,max);
	}
	
	@RequestMapping(value="/{scheduledJobId}/logs/{logId}",method=RequestMethod.GET)
	@ResponseBody
	public FmsScheduledJobLogBrief getLogBrief(@PathVariable String scheduledJobId,@PathVariable String logId) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return scheduledJobConfigService.getLog(scheduledJobId, logId);
	}
	
	@RequestMapping(value="/{scheduledJobId}/logs/{logId}/details",method=RequestMethod.GET)
	@ResponseBody
	public FmsScheduledJobLog getLogDetails(@PathVariable String scheduledJobId,@PathVariable String logId) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return scheduledJobConfigService.getLogDetails(scheduledJobId, logId);
	}
	
	
}
