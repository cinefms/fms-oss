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
import com.openfms.core.service.project.FmsStorageSystemService;
import com.openfms.model.core.storage.FmsStorageSystem;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(PackageStorageSystemController.BASE_URI)
public class PackageStorageSystemController extends GenericController<FmsStorageSystem>{

	public static final String BASE_URI = "/packages/storagesystems";
	
	@Autowired
	private FmsStorageSystemService storageSystemService;

	@Override
	protected GenericService<FmsStorageSystem> getService() {
		return storageSystemService;
	}
	
	@Override
	protected Class<FmsStorageSystem> getType() {
		return FmsStorageSystem.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsStorageSystem> getStorageSystems(
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false) String externalId, 
			@RequestParam(required=false) String locationId,
			@RequestParam(required=false) Boolean online,
			@RequestParam(required=false) Boolean permanent,
			@RequestParam(required=false) Integer status,
			@RequestParam(required=false) Integer statusAbove,
			@RequestParam(required=false) Integer statusBelow,
			@RequestParam(required=false,defaultValue="name") String order,
			@RequestParam(required=false,defaultValue="true") boolean asc,
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return storageSystemService.listStorageSystems(searchTerm,externalId,locationId,online,permanent,status,statusAbove,statusBelow,order,asc,start, max);
	}
	
	
	
}
