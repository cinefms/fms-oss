package com.openfms.core.api.v2.controllers.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openfms.core.service.project.FmsScheduledJobConfigService;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobType;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(GlobalScheduledJobTypesController.BASE_URI)
public class GlobalScheduledJobTypesController  {

	public static final String BASE_URI = "/global/scheduledjobtypes";
	
	@Autowired
	private FmsScheduledJobConfigService scheduledJobConfigService;
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsScheduledJobType> listScheduledJobType(
			@RequestParam(required=false)  String searchTerm
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return scheduledJobConfigService.listScheduledJobTypes(searchTerm);
	}
	
	@RequestMapping(value="/{scheduledJobTypeId}",method=RequestMethod.GET)
	@ResponseBody
	public FmsScheduledJobType setPlaybackDeviceCredentials(@PathVariable String scheduledJobTypeId) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		return scheduledJobConfigService.getScheduledJobType(scheduledJobTypeId);
	}
	
	
}
