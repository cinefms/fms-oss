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
import com.openfms.core.service.project.FmsCommentService;
import com.openfms.model.core.global.FmsComment;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(GlobalCommentController.BASE_URI)
public class GlobalCommentController  extends GenericController<FmsComment>{

	public static final String BASE_URI = "/global/comments";
	
	@Autowired
	private FmsCommentService fmsCommentService;
	
	@Override
	protected GenericService<FmsComment> getService() {
		return fmsCommentService;
	}

	@Override
	protected Class<FmsComment> getType() {
		return FmsComment.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsComment> listComments(
			@RequestParam(required=false) String objectId,
			@RequestParam(required=false,defaultValue="true") boolean asc,
			@RequestParam(required=false,defaultValue="date") String order,
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return fmsCommentService.listComments(objectId,asc,order,start,max, null);
	}
	
	
}
