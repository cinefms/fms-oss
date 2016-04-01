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
import com.openfms.core.service.project.FmsMediaClipTaskTypeService;
import com.openfms.model.core.movie.FmsMediaClipTaskType;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(MediaClipTaskTypeController.BASE_URI)
public class MediaClipTaskTypeController extends GenericController<FmsMediaClipTaskType> {

	public static final String BASE_URI = "/mediaclips/tasktypes";

	@Autowired
	private FmsMediaClipTaskTypeService mediaClipTaskTypeService;
	
	@Override
	protected GenericService<FmsMediaClipTaskType> getService() {
		return mediaClipTaskTypeService;
	}

	@Override
	protected Class<FmsMediaClipTaskType> getType() {
		return FmsMediaClipTaskType.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMediaClipTaskType> list(
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false) String previousTaskType,
			@RequestParam(required=false) String mediaClipType,
			@RequestParam(required=false) Boolean mandatory,
			@RequestParam(required=false) String order, 
			@RequestParam(required=false, defaultValue="true") boolean asc, 
			Integer start, 
			Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		
		return mediaClipTaskTypeService.list(
				searchTerm, 
				previousTaskType,
				mediaClipType,
				mandatory,
				order, 
				asc, 
				start, 
				max);
	}
	
	
	
}
