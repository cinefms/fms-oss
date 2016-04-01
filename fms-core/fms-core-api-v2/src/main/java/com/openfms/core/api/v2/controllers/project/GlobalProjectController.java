package com.openfms.core.api.v2.controllers.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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
import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfms.core.api.v2.utils.CorsHeaderUtil;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.admin.ProjectService;
import com.openfms.core.service.project.FmsHistoryService;
import com.openfms.core.service.project.FmsMailService;
import com.openfms.core.service.project.FmsMailTemplateService;
import com.openfms.core.service.project.ProjectSettingService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.config.FmsCredentials;
import com.openfms.model.core.global.FmsUpdateResult;
import com.openfms.model.core.project.FmsMailServerConfig;
import com.openfms.model.core.project.FmsMailTemplate;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectHolder;
import com.openfms.model.core.project.FmsProjectSettings;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.RequestedEntityTooLargeException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(GlobalProjectController.BASE_URI)
public class GlobalProjectController {

	public static final String BASE_URI = "/project";
	
	@Autowired
	private FmsHistoryService historyService;
	
	@Autowired
	private FmsMailService mailConfigService;

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProjectSettingService projectSettingService;
	
	@Autowired
	private FmsMailTemplateService mailTemplateService;
	
	@Autowired
	private AuthzService authzService;
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	@ApiDescription("Returns the current project.")
	public FmsProject getProject() throws RequestedEntityTooLargeException, DatabaseException, EntityNotFoundException {
		return FmsProjectHolder.get();
	}

	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	@ApiDescription("Returns the current project.")
	public List<FmsUpdateResult> updateAll() throws AccessDeniedException {
		return projectService.updateAll();
	}
	
	@RequestMapping(value="/settings",method=RequestMethod.GET)
	@ResponseBody
	public FmsProjectSettings etMailConfig() throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		return projectSettingService.getSettings();
	}
	
	@RequestMapping(value="/settings",method=RequestMethod.PUT)
	@ResponseBody
	public FmsProjectSettings getProjectSettings(@Valid @RequestBody FmsProjectSettings settings) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		return projectSettingService.saveSettings(settings);
	}
	
	@RequestMapping(value="/mailserver",method=RequestMethod.PUT)
	@ResponseBody
	public FmsMailServerConfig saveMailServerConfig(@Valid @RequestBody FmsMailServerConfig config) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		return projectSettingService.saveMailServerConfig(config);  
	}
	
	@RequestMapping(value="/mailserver",method=RequestMethod.GET)
	@ResponseBody
	public FmsMailServerConfig getMailServerConfig() throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		return projectSettingService.getMailServerConfig();
	}

	
	
	@RequestMapping(value="/mailserver/credentials",method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public void setProjectSettings(@Valid @RequestBody FmsCredentials credentials) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		projectSettingService.saveCredentials(credentials);  
	}

	@RequestMapping(value="/mailtemplates",method=RequestMethod.OPTIONS)
	@ResponseBody
	public void templateCollectionOptions(HttpServletResponse response) throws AccessDeniedException, DatabaseException {
		List<HttpMethod> methods = new ArrayList<HttpMethod>();
		methods.add(HttpMethod.OPTIONS);
		try {
			if(authzService.allowAccess(FmsMailTemplate.class, AccessType.READ)) {
				methods.add(HttpMethod.GET);
			}
		} catch (Exception e) {
		}
		try {
			if(authzService.allowAccess(FmsMailTemplate.class, AccessType.CREATE)) {
				methods.add(HttpMethod.POST);
			}
		} catch (Exception e) {
		}
		response.addDateHeader("expires", System.currentTimeMillis()+3600000l);
		CorsHeaderUtil.setCorsAllowedMethodHeaders(response, methods);
	}

	@RequestMapping(value="/mailtemplates/{id}",method=RequestMethod.OPTIONS)
	@ResponseBody
	public void templateObjectOptions(HttpServletResponse response, @PathVariable String id) throws AccessDeniedException, DatabaseException {
		List<HttpMethod> methods = new ArrayList<HttpMethod>();
		methods.add(HttpMethod.OPTIONS);
		try {
			if(authzService.allowAccess(FmsMailTemplate.class, id, AccessType.READ)) {
				methods.add(HttpMethod.GET);
			}
		} catch (Exception e) {
		}
		try {
			if(authzService.allowAccess(FmsMailTemplate.class, id, AccessType.UPDATE)) {
				methods.add(HttpMethod.PUT);
			}
		} catch (Exception e) {
		}
		try {
			if(authzService.allowAccess(FmsMailTemplate.class, id, AccessType.DELETE)) {
				methods.add(HttpMethod.DELETE);
			}
		} catch (Exception e) {
		}
		CorsHeaderUtil.setCorsAllowedMethodHeaders(response, methods);
	}
	
	
	
	@RequestMapping(value="/mailtemplates",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMailTemplate> getMailTemplates(
			@RequestParam(required=false) String name, 
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false,defaultValue="true") boolean asc,
			@RequestParam(required=false,defaultValue="name") String order,
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		
		DBStoreQuery q = BasicQuery.createQuery();
		if(searchTerm!=null) {
			q = q.contains("name", searchTerm);
		}

		if(name!=null) {
			q = q.eq("name", name);
		}
		
		q = q.start(start);
		q = q.max(max);
		q = q.order(order,asc);
		
		return mailTemplateService.list(q);  
	}
	
	@RequestMapping(value="/mailtemplates",method=RequestMethod.POST)
	@ResponseBody
	public FmsMailTemplate createMailTemplate(@Valid @RequestBody FmsMailTemplate template) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		return mailTemplateService.save(template);
	}
	
	@RequestMapping(value="/mailtemplates/{id}",method=RequestMethod.PUT)
	@ResponseBody
	public FmsMailTemplate updateMailTemplate(@PathVariable String id, @Valid @RequestBody FmsMailTemplate template) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		template.setId(id);
		return mailTemplateService.save(template);
	}

	@RequestMapping(value="/mailtemplates/{id}",method=RequestMethod.GET)
	@ResponseBody
	public FmsMailTemplate getMailTemplate(@PathVariable String id) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		return mailTemplateService.get(id);
	}
	

	@RequestMapping(value="/mailtemplates/{id}/test",method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public void sendTestMail(@PathVariable String id, @RequestBody Map<String,Object> params, @RequestParam String recipient) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException, IOException {
		System.err.println("PARAMS: "+(new ObjectMapper().writeValueAsString(params)));
		
		mailTemplateService.sendTest(id,recipient,params);
	}
	
	
	
	
	
	
}
