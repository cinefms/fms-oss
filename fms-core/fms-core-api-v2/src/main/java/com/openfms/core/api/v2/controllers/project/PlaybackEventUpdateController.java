package com.openfms.core.api.v2.controllers.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsEventAutoAssignService;
import com.openfms.model.core.playback.FmsEventAutoAssign;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

@RestController
@RequestMapping(PlaybackEventUpdateController.BASE_URI)
public class PlaybackEventUpdateController {

	public static final String BASE_URI = "/playback/event_updates";

	@Autowired
	private FmsEventAutoAssignService eventAutoAssignService;

	@RequestMapping(value="",method=RequestMethod.GET)
	public List<FmsEventAutoAssign> list() throws AccessDeniedException, DatabaseException {
		DBStoreQuery q = BasicQuery.createQuery();
		q.order("startDate",false);
		return eventAutoAssignService.list(q);
	}

	@RequestMapping(value="",method=RequestMethod.POST)
	public FmsEventAutoAssign create(@RequestBody(required=false) FmsEventAutoAssign a) throws AccessDeniedException, DatabaseException, VersioningException {
		if(a == null) {
			a = new FmsEventAutoAssign();
		}
		a.setId(null);
		return eventAutoAssignService.save(a);
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public FmsEventAutoAssign get(@PathVariable String id) throws AccessDeniedException, DatabaseException, VersioningException, EntityNotFoundException {
		return eventAutoAssignService.get(id);
	}
	
	
}
