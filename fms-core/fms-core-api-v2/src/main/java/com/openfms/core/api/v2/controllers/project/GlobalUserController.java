package com.openfms.core.api.v2.controllers.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsProjectUserService;
import com.openfms.model.core.auth.FmsAuthentication;
import com.openfms.model.core.auth.FmsProjectUser;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(GlobalUserController.BASE_URI)
public class GlobalUserController extends GenericController<FmsProjectUser> {

	public static final String BASE_URI = "/global/users";
	
	@Autowired
	private FmsProjectUserService projectUserService;
	
	@Override
	protected GenericService<FmsProjectUser> getService() {
		return projectUserService;
	}
	
	@Override
	protected Class<FmsProjectUser> getType() {
		return FmsProjectUser.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsProjectUser> list(
			@RequestParam(required=false) String movieId, 
			@RequestParam(required=false) String username, 
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false,defaultValue="true") boolean asc, 
			@RequestParam(required=false) String order, 
			@RequestParam(required=false) Integer start, 
			@RequestParam(required=false,defaultValue="25") int max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if(searchTerm!=null) {
			q = q.contains("searchable", searchTerm);
		}
		if(username!=null) {
			q = q.eq("username", username);
		}
		if(movieId!=null) {
			q = q.in("movieIds", movieId);
		}
		q = q.order(order!=null?order:"name",asc);
		q = q.start(start == null ? 0 : start);
		q = q.max(max);
		return getService().list(q);
	}

	
	@RequestMapping(value="/{userId}/reset",method=RequestMethod.POST)
	@ResponseBody
	public void reset(@RequestBody FmsAuthentication auth) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		if(auth.getType()==null) {
			auth.setType("passwordReset");
		}
		projectUserService.reset(auth);
	}
	
	@RequestMapping(value="/{userId}/reset",method=RequestMethod.DELETE)
	@ResponseBody
	public void reset(@PathVariable String userId) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
	}
	
	
}
