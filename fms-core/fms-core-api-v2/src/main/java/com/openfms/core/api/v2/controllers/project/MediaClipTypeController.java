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
import com.openfms.core.service.project.FmsMediaClipTypeService;
import com.openfms.model.core.movie.FmsMediaClipType;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(MediaClipTypeController.BASE_URI)
public class MediaClipTypeController extends GenericController<FmsMediaClipType> {

	public static final String BASE_URI = "/mediaclips/types";
	
	@Autowired
	private FmsMediaClipTypeService mediaClipTypeService;
	
	@Override
	protected GenericService<FmsMediaClipType> getService() {
		return mediaClipTypeService;
	}

	@Override
	protected Class<FmsMediaClipType> getType() {
		return FmsMediaClipType.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMediaClipType> list(
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false) String order, 
			@RequestParam(required=false,defaultValue="true") boolean asc, 
			@RequestParam(required=false) Integer start, 
			@RequestParam(required=false,defaultValue="25") int max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return mediaClipTypeService.list(getBasicQuery(searchTerm, asc, order, start, max));
	}
	
}
