package com.openfms.core.api.v2.controllers.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openfms.core.service.project.FmsProjectGroupService;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(GlobalGroupAdminController.BASE_URI)
public class GlobalGroupAdminController {

	public static final String BASE_URI = "/global";
	
	@Autowired
	private FmsProjectGroupService projectGroupService;
	
	
	@RequestMapping(value="/entities",method=RequestMethod.GET)
	@ResponseBody
	public List<String> listEntities() throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return projectGroupService.getControlledEntities();
	}
	
	@RequestMapping(value="/entitygroups",method=RequestMethod.GET)
	@ResponseBody
	public List<String> listEntityGroups(@RequestParam(required=false) String searchTerm) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return projectGroupService.getControlledEntityGroups(null);
	}
	
	@RequestMapping(value="/accesstypes",method=RequestMethod.GET)
	@ResponseBody
	public List<String> listAccessTypes(@RequestParam(required=false) String searchTerm) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return projectGroupService.getAccessTypes(null);
	}
	
	
	
}
