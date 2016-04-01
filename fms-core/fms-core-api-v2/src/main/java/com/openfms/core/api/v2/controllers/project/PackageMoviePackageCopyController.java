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
import com.openfms.core.service.project.FmsMoviePackageCopyService;
import com.openfms.model.core.storage.FmsMoviePackageCopy;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(PackageMoviePackageCopyController.BASE_URI)
public class PackageMoviePackageCopyController extends GenericController<FmsMoviePackageCopy> {

	public static final String BASE_URI = "/packages/packagecopies";
	
	
	@Autowired
	private FmsMoviePackageCopyService moviePackageCopyService;

	@Override
	protected GenericService<FmsMoviePackageCopy> getService() {
		return moviePackageCopyService;
	}
	
	@Override
	protected Class<FmsMoviePackageCopy> getType() {
		return FmsMoviePackageCopy.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMoviePackageCopy> getMoviePackageCopys(
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false) String moviePackageId, 
			@RequestParam(required=false) String externalId, 
			@RequestParam(required=false) Integer status,
			@RequestParam(required=false) Integer statusAbove,
			@RequestParam(required=false) Integer statusBelow,
			@RequestParam(required=false) String storageSystemId,
			@RequestParam(required=false,defaultValue="name") String order, 
			@RequestParam(required=false,defaultValue="true") boolean asc, 
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return moviePackageCopyService.listMoviePackageCopies(searchTerm,moviePackageId,externalId,status,statusAbove,statusBelow,storageSystemId,order, asc, start, max);
	}
	
	
	
}
