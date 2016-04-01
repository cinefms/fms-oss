package com.openfms.core.api.v2.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.api.v2.utils.CorsHeaderUtil;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsHistoryService;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsHistory;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

public abstract class GenericController<T extends FmsObject> {
	
	@Autowired
	private FmsHistoryService historyService;
	
	@Autowired
	private AuthzService authzService;
	
	protected abstract Class<T> getType();
	protected abstract GenericService<T> getService();
	
	protected List<T> list(String searchTerm, Boolean asc, String order, Integer start, Integer max) throws AccessDeniedException, DatabaseException, EntityNotFoundException {
		DBStoreQuery q = getBasicQuery(searchTerm, asc, order, start, max);
		return getService().list(q);
	}
	
	@RequestMapping(value="",method=RequestMethod.OPTIONS)
	@ResponseBody
	public void collectionOptions(HttpServletResponse response, HttpServletRequest request) throws AccessDeniedException, DatabaseException {
		List<HttpMethod> methods = new ArrayList<HttpMethod>();
		methods.add(HttpMethod.OPTIONS);
		try {
			if(authzService.allowAccess(getType(), AccessType.READ)) {
				methods.add(HttpMethod.GET);
			}
		} catch (Exception e) {
		}
		try {
			if(authzService.allowAccess(getType(), AccessType.CREATE)) {
				methods.add(HttpMethod.POST);
			}
		} catch (Exception e) {
		}
		response.addDateHeader("expires", System.currentTimeMillis()+3600000l);
		CorsHeaderUtil.setCorsAllowedMethodHeaders(response, methods);
	}
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.OPTIONS)
	@ResponseBody
	public void objectOptions(HttpServletResponse response, @PathVariable String id) throws AccessDeniedException, DatabaseException {
		List<HttpMethod> methods = new ArrayList<HttpMethod>();
		methods.add(HttpMethod.OPTIONS);
		try {
			if(authzService.allowAccess(getType(), id, AccessType.READ)) {
				methods.add(HttpMethod.GET);
			}
		} catch (Exception e) {
		}
		try {
			if(authzService.allowAccess(getType(), id, AccessType.UPDATE)) {
				methods.add(HttpMethod.PUT);
			}
		} catch (Exception e) {
		}
		try {
			if(authzService.allowAccess(getType(), id, AccessType.DELETE)) {
				methods.add(HttpMethod.DELETE);
			}
		} catch (Exception e) {
		}
		CorsHeaderUtil.setCorsAllowedMethodHeaders(response, methods);
	}
	
	@RequestMapping(value="",method=RequestMethod.POST)
	@ResponseBody
	public T create(@Valid @RequestBody T body) throws AccessDeniedException, DatabaseException, VersioningException {
		body.setId(null);
		return getService().save(body);
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	@ResponseBody
	public T get(@PathVariable String id) throws AccessDeniedException, DatabaseException, EntityNotFoundException {
		return getService().get(id);
	}

	@RequestMapping(value="/{id}/history",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsHistory> getHistory(
			@PathVariable String id, 
			@RequestParam(required=false,defaultValue="0") Integer start, 
			@RequestParam(required=false,defaultValue="50") Integer max 
		) throws AccessDeniedException, DatabaseException, EntityNotFoundException {
		return historyService.listHistory(id, "date", false, start, max);
	}

	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	@ResponseBody
	public T save(@PathVariable String id, @Valid @RequestBody T body) throws AccessDeniedException, DatabaseException, VersioningException {
		body.setId(id);
		return getService().save(body);
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String id) throws AccessDeniedException, DatabaseException {
		getService().delete(id);
	}
	
	protected DBStoreQuery getBasicQuery(String searchTerm, boolean asc, String order, Integer start, Integer max) {
		DBStoreQuery q = BasicQuery.createQuery();
		if(searchTerm!=null) {
			q = q.contains("searchable", searchTerm);
		}
		q = q.order(order!=null?order:"name",asc);
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? 25 : max);
		return q;
	}
	
	
}
