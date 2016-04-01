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
import com.openfms.core.service.project.FmsFileCopyService;
import com.openfms.model.core.storage.FmsFileCopy;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(PackageFileCopyController.BASE_URI)
public class PackageFileCopyController extends GenericController<FmsFileCopy> {

	public static final String BASE_URI = "/packages/filecopies";
	
	
	@Autowired
	private FmsFileCopyService fileCopyService;
	
	@Override
	protected GenericService<FmsFileCopy> getService() {
		return fileCopyService;
	}
	
	@Override
	protected Class<FmsFileCopy> getType() {
		return FmsFileCopy.class;
	}

	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsFileCopy> getFileCopys(
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false) String packageId, 
			@RequestParam(required=false) String packageCopyId, 
			@RequestParam(required=false) String fileId, 
			@RequestParam(required=false) String lastUpdateBefore, 
			@RequestParam(required=false) String lastUpdateAfter, 
			@RequestParam(required=false,defaultValue="name") String order, 
			@RequestParam(required=false,defaultValue="true") boolean asc, 
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return fileCopyService.listFileCopies(searchTerm,packageId,packageCopyId,fileId,lastUpdateBefore,lastUpdateAfter,order,asc,start,max);
	}
	
	
	
}
