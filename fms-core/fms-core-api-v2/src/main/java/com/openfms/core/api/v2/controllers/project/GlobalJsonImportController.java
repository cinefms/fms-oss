package com.openfms.core.api.v2.controllers.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openfms.core.service.project.ImportService;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.core.movie.ImportResult;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.core.playback.FmsSite;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.ConcurrencyException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(GlobalJsonImportController.BASE_URI)
public class GlobalJsonImportController {

	public static final String BASE_URI = "/global/import/json";
	
	@Autowired
	private ImportService importService;
	
	@RequestMapping(value="/movies",method=RequestMethod.POST)
	@ResponseBody
	public List<ImportResult> importMovies(
			@RequestBody List<FmsMovie> movies,
			@RequestParam(required=false,defaultValue="false") boolean deleteObsolete
		) throws AccessDeniedException, DatabaseException, VersioningException, EntityNotFoundException, ConcurrencyException {
		return importService.importMovies(movies,deleteObsolete);
	}

	@RequestMapping(value="/events",method=RequestMethod.POST)
	@ResponseBody
	public List<ImportResult> importScreenings(
			@RequestBody List<FmsEvent> events,
			@RequestParam(required=false,defaultValue="false") boolean deleteObsolete
		) throws AccessDeniedException, DatabaseException, VersioningException, EntityNotFoundException, InvalidParameterException, ConcurrencyException {
		return importService.importEvents(events,deleteObsolete);
	}
	
	@RequestMapping(value="/sites",method=RequestMethod.POST)
	@ResponseBody
	public List<ImportResult> importSites
		(
				@RequestBody List<FmsSite> sites,
				@RequestParam(required=false,defaultValue="false") boolean deleteObsolete
		) throws AccessDeniedException, DatabaseException, VersioningException, EntityNotFoundException {
		return importService.importSites(sites, deleteObsolete);
	}

	
	@RequestMapping(value="/locations",method=RequestMethod.POST)
	@ResponseBody
	public List<ImportResult> importLocations
		(
				@RequestBody List<FmsLocation> locations,
				@RequestParam(required=false,defaultValue="false") boolean deleteObsolete
				
		) throws AccessDeniedException, DatabaseException, VersioningException, EntityNotFoundException {
		return importService.importLocations(locations, deleteObsolete);
	}
	
	
}
