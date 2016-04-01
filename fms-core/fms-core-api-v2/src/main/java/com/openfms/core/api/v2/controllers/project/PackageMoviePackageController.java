package com.openfms.core.api.v2.controllers.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.model.core.movie.FmsMoviePackage;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(PackageMoviePackageController.BASE_URI)
public class PackageMoviePackageController extends GenericController<FmsMoviePackage> {

	public static final String BASE_URI = "/packages/packages";
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	@Override
	protected GenericService<FmsMoviePackage> getService() {
		return moviePackageService;
	}
	
	@Override
	protected Class<FmsMoviePackage> getType() {
		return FmsMoviePackage.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMoviePackage> getMoviePackages(
			@RequestParam(required=false) String searchTerm, 				
			@RequestParam(required=false,value="id") String[] ids, 				
			@RequestParam(required=false) String movieId,
			@RequestParam(required=false) String movieVersionId,
			@RequestParam(required=false) String eventId,
			@RequestParam(required=false) String type, 				
			@RequestParam(required=false) String quickhash, 				
			@RequestParam(required=false) String externalId, 				
			@RequestParam(required=false,defaultValue="name") String order, 
			@RequestParam(required=false,defaultValue="true") boolean asc, 
			@RequestParam(required=false,defaultValue="0") Integer start, 
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (searchTerm != null) {
			q = q.contains("name", searchTerm);
		}
		if (movieId != null) {
			q = q.eq("movieId", movieId);
		}
		if (movieVersionId != null) {
			q = q.eq("movieVersionIds", movieVersionId);
		}
		if (externalId != null) {
			q = q.eq("externalId", externalId);
		}
		if (eventId != null) {
			q = q.eq("events.eventId", eventId);
		}
		if (quickhash != null) {
			q = q.eq("quickhash", quickhash);
		}
		if (type != null) {
			q = q.eq("type", type);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max);
		return moviePackageService.list(q);
	}
	
	
	
}
