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
import com.openfms.core.service.project.FmsSiteService;
import com.openfms.model.core.playback.FmsSite;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(PlaybackSiteController.BASE_URI)
public class PlaybackSiteController extends GenericController<FmsSite> {

	public static final String BASE_URI = "/playback/sites";
	
	@Autowired
	private FmsSiteService siteService;

	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsSite> getSites(
			@RequestParam(required=false) String searchTerm,
			@RequestParam(required=false) String externalId,
			@RequestParam(required=false,defaultValue="name") String order,
			@RequestParam(required=false,defaultValue="true") boolean asc,
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return siteService.listSites(searchTerm,externalId,order,asc, start, max);
	}

	@Override
	protected Class<FmsSite> getType() {
		return FmsSite.class;
	}

	@Override
	protected GenericService<FmsSite> getService() {
		return siteService;
	}
	
	
	
}
