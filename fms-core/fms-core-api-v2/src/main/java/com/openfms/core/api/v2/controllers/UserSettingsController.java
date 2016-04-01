package com.openfms.core.api.v2.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.apitester.annotations.ApiDescription;
import com.openfms.core.api.v2.utils.CorsHeaderUtil;
import com.openfms.core.service.UserSettingsService;
import com.openfms.model.core.auth.FmsUserSettings;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(value="/usersettings")
public class UserSettingsController {
	
	@Autowired
	private UserSettingsService userSettingsService;

	
	@RequestMapping(value="",method=RequestMethod.OPTIONS)
	@ResponseBody
	public void settingsOptions(HttpServletResponse response) throws AccessDeniedException, DatabaseException {
		List<HttpMethod> methods = new ArrayList<HttpMethod>();
		methods.add(HttpMethod.PUT);
		methods.add(HttpMethod.GET);
		CorsHeaderUtil.setCorsAllowedMethodHeaders(response, methods);
	}
	
	
	@RequestMapping(value="", method=RequestMethod.GET)
	@ResponseBody
	@ApiDescription("Use this method to get your user's setting. Throws a 401 if not logged in.")
	public FmsUserSettings getUserSettings() throws AccessDeniedException, DatabaseException, VersioningException {
		return userSettingsService.getSettings();
	}
	
	
	@RequestMapping(value="", method=RequestMethod.PUT)
	@ResponseBody
	@ApiDescription("Use this method to save your user's setting. Throws a 401 if not logged in.")
	public FmsUserSettings getUserSettings(FmsUserSettings settings) throws AccessDeniedException, DatabaseException, VersioningException {
		return userSettingsService.saveSettings(settings);
	}
	
	

}
