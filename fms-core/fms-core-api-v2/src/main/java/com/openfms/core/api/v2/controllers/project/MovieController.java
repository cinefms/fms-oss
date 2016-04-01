package com.openfms.core.api.v2.controllers.project;

import java.util.Date;
import java.util.List;

import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.api.v2.utils.DateRange;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(MovieController.BASE_URI)
public class MovieController extends GenericController<FmsMovie>{

	public static final String BASE_URI = "/movies/movies";
	
	@Autowired
	private FmsMovieService movieService;

	@Override
	protected Class<FmsMovie> getType() {
		return FmsMovie.class;
	}

	@Override
	protected GenericService<FmsMovie> getService() {
		return movieService;
	}

	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMovie> listMovies(
			@RequestParam(value="id",required=false) List<String> ids, 
			@RequestParam(required=false) String category, 
			@RequestParam(required=false) String externalId, 
			@RequestParam(required=false) Integer[] mediaStatus,
			@RequestParam(required=false) Integer[] versionStatus,
			@RequestParam(required=false) Integer[] encryptionStatus,
			@RequestParam(required=false) Integer[] screeningStatus,
			@RequestParam(required=false) Boolean openTasks,
			@RequestParam(required=false) Boolean hasEvents,
			@RequestParam(required=false) Boolean hasVersions,
			@RequestParam(required=false) Boolean hasMediaClips,
			@RequestParam(required=false) DateRange screeningWithin,
			@RequestParam(required=false) Date dateFrom,
			@RequestParam(required=false) Date dateTo,
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false,defaultValue="name") String order, 
			@RequestParam(required=false,defaultValue="true") boolean asc, 
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
			) throws DatabaseException, EntityNotFoundException, AccessDeniedException, ValidationException {
		if(screeningWithin!=null) {
			if(dateFrom!=null || dateTo!=null) {
				throw new ValidationException("cannot do both from/to AND screeningWithin");
			}
			dateFrom = screeningWithin.getFrom();
			dateTo = screeningWithin.getTo();
		}
		return movieService.listMovies(ids, externalId, category, mediaStatus, versionStatus, encryptionStatus, screeningStatus, dateFrom, dateTo, null, openTasks, hasEvents, hasVersions, hasMediaClips, searchTerm, order, asc, start, max);
	}
	
	
	
}
