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
import com.openfms.core.service.project.FmsLocationService;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(PlaybackLocationController.BASE_URI)
public class PlaybackLocationController extends GenericController<FmsLocation> {

	public static final String BASE_URI = "/playback/locations";
	
	@Autowired
	private FmsLocationService locationService;
	
	@Override
	protected Class<FmsLocation> getType() {
		return FmsLocation.class;
	}
	
	@Override
	protected GenericService<FmsLocation> getService() {
		return locationService;
	}

	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsLocation> getLocations(
			@RequestParam(required=false) String searchTerm,
			@RequestParam(required=false,value="siteId") String[] siteIds,
			@RequestParam(required=false,value="locationId") String[] locationIds,
			@RequestParam(required=false) String externalId,
			@RequestParam(required=false, defaultValue="all") Boolean active,
			@RequestParam(required=false,defaultValue="name") String order,
			@RequestParam(required=false,defaultValue="true") boolean asc,
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return locationService.listLocations(searchTerm,siteIds,locationIds,externalId,active,order, asc, start, max);
	}
	
}
