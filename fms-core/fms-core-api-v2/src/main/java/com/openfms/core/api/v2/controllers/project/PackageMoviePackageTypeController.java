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
import com.openfms.core.service.project.FmsMoviePackageTypeService;
import com.openfms.model.core.movie.FmsMoviePackageType;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(PackageMoviePackageTypeController.BASE_URI)
public class PackageMoviePackageTypeController extends GenericController<FmsMoviePackageType> {

	public static final String BASE_URI = "/packages/packagetypes";
	protected Class<?> t = FmsMoviePackageType.class;

	@Autowired
	private FmsMoviePackageTypeService moviePackageTypeService; 
	
	@Override
	protected GenericService<FmsMoviePackageType> getService() {
		return moviePackageTypeService;
	}

	@Override
	protected Class<FmsMoviePackageType> getType() {
		return FmsMoviePackageType.class;
	}
	
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMoviePackageType> list(
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false) String order, 
			@RequestParam(required=false,defaultValue="true") boolean asc, 
			@RequestParam(required=false) Integer start, 
			@RequestParam(required=false,defaultValue="25") int max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return moviePackageTypeService.list(getBasicQuery(searchTerm, asc, order, start, max));
	}

	

}
