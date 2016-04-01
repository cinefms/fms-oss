package com.openfms.core.api.v2.controllers.project;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsKeyRequestService;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.crypto.FmsKeyRequest;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(CryptoKeyRequestController.BASE_URI)
public class CryptoKeyRequestController extends GenericController<FmsKeyRequest> {

	public static final String BASE_URI = "/crypto/keyrequests";

	@Autowired
	private FmsKeyRequestService keyRequestService;
	
	@Override
	protected Class<FmsKeyRequest> getType() {
		return FmsKeyRequest.class;
	}

	@Override
	protected GenericService<FmsKeyRequest> getService() {
		return keyRequestService;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsKeyRequest> getKeyRequests(
			@RequestParam(required=false) String searchTerm,
			@RequestParam(required=false) String movieId,
			@RequestParam(required=false) String mediaClipExternalId,
			@RequestParam(required=false) String mediaClipId,
			@RequestParam(required=false) String certificateDnQualifier,
			@RequestParam(required=false) String certificateId,
			@RequestParam(required=false) String deviceId,
			@RequestParam(required=false) String eventId,
			@RequestParam(required=false) Date endAfter,
			@RequestParam(required=false) Boolean canceled,
			@RequestParam(required=false) Boolean fulfilled,
			
			@RequestParam(required=false,defaultValue="validFrom") String order,
			@RequestParam(required=false,defaultValue="true") boolean asc,
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		
		DBStoreQuery q = getBasicQuery(searchTerm, asc, order, start, max);
		if(movieId!=null) {
			q = q.eq("movieId", movieId);
		}
		if(mediaClipExternalId!=null) {
			q = q.eq("mediaClipExternalId", mediaClipExternalId);
		}
		if(mediaClipId!=null) {
			q = q.eq("mediaClipId", mediaClipId);
		}
		if(certificateDnQualifier!=null) {
			q = q.eq("certificateDnQualifier", certificateDnQualifier);
		}
		if(certificateId!=null) {
			q = q.eq("certificateId", certificateId);
		}
		if(deviceId!=null) {
			q = q.eq("deviceId", deviceId);
		}
		if(eventId!=null) {
			q = q.eq("eventId", eventId);
		}
		if(canceled!=null) {
			q = q.eq("canceled", canceled.booleanValue());
		}
		if(fulfilled!=null) {
			q = q.eq("fulfilled", fulfilled.booleanValue());
		}
		if(endAfter!=null) {
			q = q.gte("end", endAfter);
		}
		q = q.max(max);
		
		return keyRequestService.list(q);
	}
	
	@RequestMapping(value="",method=RequestMethod.POST)
	@ResponseBody
	public FmsKeyRequest create(@Valid @RequestBody FmsKeyRequest body) throws AccessDeniedException, DatabaseException, VersioningException {
		body.setRequestedBy(FmsSessionHolder.getCurrentUser().getDisplayName());
		return getService().save(body);
	}
	
	
	@RequestMapping(value="/{id}/upload",method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public void uploadKeyRequest(@PathVariable String id, @RequestBody String key) throws AccessDeniedException, DatabaseException, EntityNotFoundException, VersioningException, IOException {
		keyRequestService.fulfillKeyRequest(id, key.getBytes("utf-8"));
	}
	
	
	
	
	
}
