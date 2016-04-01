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
import com.openfms.core.service.project.FmsPlaybackDeviceTypeService;
import com.openfms.model.core.playback.FmsPlaybackDeviceType;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(PlaybackDeviceTypeController.BASE_URI)
public class PlaybackDeviceTypeController extends GenericController<FmsPlaybackDeviceType> {

	public static final String BASE_URI = "/playback/devices_types";
	
	@Autowired
	private FmsPlaybackDeviceTypeService playbackDeviceTypeService;
	
	@Override
	protected GenericService<FmsPlaybackDeviceType> getService() {
		return playbackDeviceTypeService;
	}

	@Override
	protected Class<FmsPlaybackDeviceType> getType() {
		return FmsPlaybackDeviceType.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsPlaybackDeviceType> list(
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false) String order, 
			@RequestParam(required=false,defaultValue="true") boolean asc, 
			@RequestParam(required=false) Integer start, 
			@RequestParam(required=false,defaultValue="25") int max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return playbackDeviceTypeService.list(getBasicQuery(searchTerm, asc, order, start, max));
	}
	
}
