package com.openfms.core.api.v2.controllers.admin;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cinefms.apitester.annotations.ApiDescription;
import com.openfms.core.api.v2.AdminController;
import com.openfms.core.service.admin.ProjectService;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;
import com.skjlls.aspects.metrics.annotations.Metrics;

@Controller
@RequestMapping(value="/admin/projects")
public class ProjectAdminController implements AdminController {
	
	@Autowired(required=true)
	private ProjectService projectService;
	
	@Metrics
	@RequestMapping(value="", method=RequestMethod.GET)
	@ResponseBody
	@ApiDescription("Use this method to get a list of projects.")
	public List<FmsProject> getProjects(@RequestParam(required=false) String searchTerm, @RequestParam(required=false,defaultValue="0") Integer start, @RequestParam(required=false,defaultValue="10") Integer max) throws AccessDeniedException, DatabaseException {
		return projectService.getProjects(searchTerm,start,max);
	}

	@Metrics
	@RequestMapping(value="", method=RequestMethod.POST)
	@ResponseBody
	@ApiDescription("Use this method to get one sepcific project.")
	public FmsProject createProject(@Valid @RequestBody FmsProject project) throws AccessDeniedException, DatabaseException, VersioningException {
		project.setId(null);
		return projectService.saveProject(project);
	}
	

	@Metrics
	@RequestMapping(value="/{projectId}", method=RequestMethod.GET)
	@ResponseBody
	@ApiDescription("Use this method to get one sepcific project.")
	public FmsProject getProject(@PathVariable String projectId) throws AccessDeniedException, DatabaseException, EntityNotFoundException {
		return projectService.getProject(projectId);
	}
	
	@Metrics
	@RequestMapping(value="/{projectId}", method=RequestMethod.PUT)
	@ResponseBody
	@ApiDescription("Use this method to get one sepcific project.")
	public FmsProject updateProject(@PathVariable String projectId, @Valid @RequestBody FmsProject project) throws AccessDeniedException, DatabaseException, VersioningException  {
		project.setId(projectId);
		return projectService.saveProject(project);
	}
	
	@Metrics
	@RequestMapping(value="/{projectId}", method=RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiDescription("Use this method to delete one sepcific project.")
	public void deleteProject(@PathVariable String projectId) throws AccessDeniedException, DatabaseException {
		projectService.deleteProject(projectId);
	}
	

}
