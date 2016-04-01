package com.openfms.core.api.v2.controllers.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.api.v2.utils.CorsHeaderUtil;
import com.openfms.core.api.v2.utils.DateRange;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.project.FmsMediaClipTaskService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.movie.FmsMediaClipTask;
import com.openfms.model.core.movie.FmsMediaClipTaskProgress;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(MediaClipTaskController.BASE_URI)
public class MediaClipTaskController {

	public static final String BASE_URI = "/mediaclips/tasks";
	
	
	@Autowired
	private FmsMediaClipTaskService mediaClipTaskService;
	
	@Autowired
	private AuthzService authzService;
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	@ResponseBody
	public FmsMediaClipTask getClipTasks(@PathVariable String id) throws AccessDeniedException, DatabaseException, EntityNotFoundException {
		return mediaClipTaskService.get(id);
	}

	@RequestMapping(value="",method=RequestMethod.POST)
	@ResponseBody
	public FmsMediaClipTask createClipTasks(@Valid @RequestBody FmsMediaClipTask task) throws AccessDeniedException, DatabaseException, EntityNotFoundException, VersioningException {
		task.setId(null);
		return mediaClipTaskService.save(task);
	}

			
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMediaClipTask> listMediaClipTasks(
			@RequestParam(required=false) String mediaClipId,
			@RequestParam(required=false) String userId,
			@RequestParam(required=false) String assignedTo,
			@RequestParam(required=false) String deviceId,
			@RequestParam(required=false) Boolean assigned,
			@RequestParam(required=false,value="type") String[] types,
			@RequestParam(required=false) Integer[] status,
			@RequestParam(required=false) Integer[] priority,
			@RequestParam(required=false) Integer statusAbove,
			@RequestParam(required=false) Integer statusBelow,
			@RequestParam(required=false, defaultValue="false") Boolean closed,
			@RequestParam(required=false) DateRange screeningWithin,
			@RequestParam(required=false) Date usedAfter,
			@RequestParam(required=false) String order,
			@RequestParam(required=false,defaultValue="true") boolean asc,
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (mediaClipId != null) {
			q = q.eq("mediaClipId", mediaClipId);
		}
		if (userId != null) {
			q = q.eq("userId", userId);
		}
		if (closed != null && closed.booleanValue()) {
		} else {
			q = q.eq("closed", false);
		}
		if (assignedTo != null) {
			if(assignedTo.compareTo("me")==0) {
				assignedTo = FmsSessionHolder.getCurrentUser().getId();
			}
			q = q.eq("userId", assignedTo);
		}
		if(assigned!=null) {
			if(assigned.booleanValue()) {
				q = q.ne("userId", null);
			} else {
				q = q.eq("userId", null);
			}
		}
		
		if(screeningWithin!=null) {
			if(screeningWithin.getFrom()!=null) {
				q = q.gte("firstEventDate", screeningWithin.getFrom());
			}
			if(screeningWithin.getTo()!=null) {
				q = q.lte("firstEventDate", screeningWithin.getTo());
			}
		}
		
		if (types != null) {
			q = q.in("type", types);
		}
		if (deviceId != null) {
			q = q.eq("deviceId", deviceId);
		}
		if (usedAfter != null) {
			q = q.gte("lastEventDate", usedAfter);
		}
		if (status != null && status.length > 0) {
			q = q.in("status", status);
		}
		if (priority != null && priority.length > 0) {
			q = q.in("priority", priority);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max);
		
		return mediaClipTaskService.list(q);
	}
	
	@RequestMapping(value="/{mediaClipTaskId}/progress",method=RequestMethod.POST)
	@ResponseBody
	public FmsMediaClipTaskProgress addProgress(@PathVariable String mediaClipTaskId, @Valid @RequestBody FmsMediaClipTaskProgress progress) throws AccessDeniedException, DatabaseException, EntityNotFoundException, VersioningException {
		progress.setMediaClipTaskId(mediaClipTaskId);
		return mediaClipTaskService.addProgress(progress);
	}

	@RequestMapping(value="/{mediaClipTaskId}/progress",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMediaClipTaskProgress> getProgress(@PathVariable String mediaClipTaskId) throws AccessDeniedException, DatabaseException, EntityNotFoundException {
		return mediaClipTaskService.getProgress(mediaClipTaskId);
	}

	@RequestMapping(value="",method=RequestMethod.OPTIONS)
	@ResponseBody
	public void collectionOptions(HttpServletResponse response) throws AccessDeniedException, DatabaseException {
		List<HttpMethod> methods = new ArrayList<HttpMethod>();
		methods.add(HttpMethod.OPTIONS);
		try {
			if(authzService.allowAccess(FmsMediaClipTask.class, AccessType.READ)) {
				methods.add(HttpMethod.GET);
			}
		} catch (Exception e) {
		}
		try {
			if(authzService.allowAccess(FmsMediaClipTask.class, AccessType.CREATE)) {
				methods.add(HttpMethod.POST);
			}
		} catch (Exception e) {
		}
		response.addDateHeader("expires", System.currentTimeMillis()+3600000l);
		CorsHeaderUtil.setCorsAllowedMethodHeaders(response, methods);
	}
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.OPTIONS)
	@ResponseBody
	public void objectOptions(HttpServletResponse response, @PathVariable String id) throws AccessDeniedException, DatabaseException {
		List<HttpMethod> methods = new ArrayList<HttpMethod>();
		methods.add(HttpMethod.OPTIONS);
		try {
			if(authzService.allowAccess(FmsMediaClipTask.class, id, AccessType.READ)) {
				methods.add(HttpMethod.GET);
			}
		} catch (Exception e) {
		}
		try {
			if(authzService.allowAccess(FmsMediaClipTask.class, id, AccessType.UPDATE)) {
				methods.add(HttpMethod.PUT);
			}
		} catch (Exception e) {
		}
		try {
			if(authzService.allowAccess(FmsMediaClipTask.class, id, AccessType.DELETE)) {
				methods.add(HttpMethod.DELETE);
			}
		} catch (Exception e) {
		}
		CorsHeaderUtil.setCorsAllowedMethodHeaders(response, methods);
	}

	
}
