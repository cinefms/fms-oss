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
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.model.core.movie.FmsMovieVersion;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(MovieVersionController.BASE_URI)
public class MovieVersionController extends GenericController<FmsMovieVersion> {

	public static final String BASE_URI = "/movies/versions";
	
	@Autowired
	private FmsMovieVersionService movieVersionService;

	@Override
	protected GenericService<FmsMovieVersion> getService() {
		return movieVersionService;
	}
	
	@Override
	protected Class<FmsMovieVersion> getType() {
		return FmsMovieVersion.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMovieVersion> listMovieVersions(
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false) String externalId, 
			@RequestParam(required=false) String movieId, 
			@RequestParam(required=false) String mediaClipId, 
			@RequestParam(required=false) Integer statusBelow, 
			@RequestParam(required=false) Integer statusAbove, 
			@RequestParam(required=false,defaultValue="0") Integer start, 
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return movieVersionService.listMovieVersions(searchTerm, movieId, externalId, mediaClipId, statusBelow, statusAbove, start, max);
	}
	
	
}
