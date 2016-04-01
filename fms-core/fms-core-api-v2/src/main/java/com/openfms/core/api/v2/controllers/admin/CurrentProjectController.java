package com.openfms.core.api.v2.controllers.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cinefms.apitester.annotations.ApiDescription;
import com.cinefms.dbstore.api.exceptions.DBStoreException;
import com.openfms.core.api.v2.AdminController;
import com.openfms.core.service.admin.ProjectService;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectHolder;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.skjlls.aspects.metrics.annotations.Metrics;

@Controller
@RequestMapping(value="/admin/currentproject")
public class CurrentProjectController implements AdminController {
	
	private static Log log = LogFactory.getLog(CurrentProjectController.class);
	
	@Autowired(required=true)
	private ProjectService projectService;
	
	@Metrics
	@RequestMapping(value="", method=RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ApiDescription("this returns a brief version of the project (basically the name only) or throws a 404 if no current project is set.")
	public FmsProject getCurrentProject(HttpServletRequest req) throws AccessDeniedException, DBStoreException, EntityNotFoundException {
		
		if(FmsProjectHolder.get()!=null) {
			FmsProject out = new FmsProject();
			out.setName(FmsProjectHolder.get().getName());
			out.setShortName(FmsProjectHolder.get().getShortName());
			out.setTimeFormat(FmsProjectHolder.get().getTimeFormat());
			out.setTimezone(FmsProjectHolder.get().getTimezone());
			return out;
		}
		
		log.warn("no current project ... ");
		
		throw new EntityNotFoundException(FmsProject.class,null);
	}
	

}
