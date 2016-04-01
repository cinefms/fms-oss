package com.openfms.core.api.v2.controllers.project;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.api.v2.utils.CorsHeaderUtil;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.project.FmsMOTDService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.global.FmsMOTD;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(GlobalMOTDController.BASE_URI)
public class GlobalMOTDController {

	public static final String BASE_URI = "/global/motd";
	
	@Autowired
	private FmsMOTDService motdService;

	@Autowired
	private AuthzService authzService;
	
	@RequestMapping(value="",method=RequestMethod.OPTIONS)
	@ResponseBody
	public void collectionOptions(HttpServletResponse response) throws AccessDeniedException, DatabaseException {
		List<HttpMethod> methods = new ArrayList<HttpMethod>();
		methods.add(HttpMethod.OPTIONS);
		try {
			if(authzService.allowAccess(FmsMOTD.class, AccessType.READ)) {
				methods.add(HttpMethod.GET);
			}
		} catch (Exception e) {
		}
		try {
			if(authzService.allowAccess(FmsMOTD.class, AccessType.CREATE)) {
				methods.add(HttpMethod.POST);
			}
		} catch (Exception e) {
		}
		response.addDateHeader("expires", System.currentTimeMillis()+3600000l);
		CorsHeaderUtil.setCorsAllowedMethodHeaders(response, methods);
	}

	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMOTD> listMessages(
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		q = q.order("date",false);
		return motdService.list(q);
	}
	
	@RequestMapping(value="",method=RequestMethod.POST)
	@ResponseBody
	public FmsMOTD addMessage(
			@Valid @RequestBody FmsMOTD message
			) throws InvalidParameterException, DatabaseException, EntityNotFoundException, AccessDeniedException, VersioningException {
		return motdService.save(message);
	}
	
	
}
