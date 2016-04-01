package com.openfms.core.api.v2.controllers.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsProjectGroupService;
import com.openfms.model.core.auth.FmsGroup;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(GlobalGroupController.BASE_URI)
public class GlobalGroupController extends GenericController<FmsGroup> {

	public static final String BASE_URI = "/global/groups";
	
	@Autowired
	private FmsProjectGroupService projectGroupService;
	
	@Override
	protected GenericService<FmsGroup> getService() {
		return projectGroupService;
	}
	
	@Override
	protected Class<FmsGroup> getType() {
		return FmsGroup.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsGroup> list(
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false,defaultValue="true") boolean asc, 
			@RequestParam(required=false) String order, 
			@RequestParam(required=false) Integer start, 
			@RequestParam(required=false,defaultValue="25") int max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return super.list(searchTerm,asc,order,start,max);
	}
	
	
	
}
