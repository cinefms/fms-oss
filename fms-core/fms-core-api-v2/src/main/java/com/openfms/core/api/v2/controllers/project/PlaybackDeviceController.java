package com.openfms.core.api.v2.controllers.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.project.FmsPlaybackDeviceService;
import com.openfms.model.core.playback.FmsCinemaServerStatus;
import com.openfms.model.core.playback.FmsCinemaServerStatusBrief;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(PlaybackDeviceController.BASE_URI)
public class PlaybackDeviceController extends GenericController<FmsPlaybackDevice> {

	public static final String BASE_URI = "/playback/devices";
	
	@Autowired
	private FmsPlaybackDeviceService playbackDeviceService;
	
	@Override
	protected FmsPlaybackDeviceService getService() {
		return playbackDeviceService;
	}
	
	@Override
	protected Class<FmsPlaybackDevice> getType() {
		return FmsPlaybackDevice.class;
	}
	
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsPlaybackDevice> listPlaybackDevices(
			@RequestParam(required=false)  String searchTerm,
			@RequestParam(required=false)  String locationId,
			@RequestParam(required=false)  String mediaClipType,
			@RequestParam(required=false)  String vendor,
			@RequestParam(required=false)  String model,
			@RequestParam(required=false)  String serial,
			@RequestParam(required=false,defaultValue="name")  String order,
			@RequestParam(required=false,defaultValue="true")  boolean asc,
			@RequestParam(required=false,defaultValue="0") int start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (locationId != null) {
			q = q.eq("locationId", locationId);
		}
		if (searchTerm != null) {
			List<DBStoreQuery> qs = new ArrayList<DBStoreQuery>();
			qs.add(BasicQuery.createQuery().contains("name", searchTerm));
			qs.add(BasicQuery.createQuery().contains("serial", searchTerm));
			qs.add(BasicQuery.createQuery().contains("vendor", searchTerm));
			qs.add(BasicQuery.createQuery().contains("model", searchTerm));
			q = q.or(qs);
		}
		if(mediaClipType != null) {
			q = q.in("mediaClipTypes", mediaClipType);
		}
		if (vendor != null) {
			q = q.eq("vendor", vendor);
		}
		if (model != null) {
			q = q.eq("model", model);
		}
		if (serial != null) {
			q = q.eq("serial", serial);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start);
		q = q.max(max);
		return playbackDeviceService.list(q);
	}
	
	@RequestMapping(value="/{playbackDeviceId}/status",method=RequestMethod.POST)
	@ResponseBody
	public FmsCinemaServerStatus savePlaybackDeviceStatus(@PathVariable String playbackDeviceId, @RequestBody FmsCinemaServerStatus status) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		status.setDeviceId(playbackDeviceId);
		status.setDate(new Date());
		return getService().savePlaybackDeviceStatus(status);
	}
	
	@RequestMapping(value="/{playbackDeviceId}/status",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsCinemaServerStatusBrief> getPlaybackDeviceCredentials(
			@PathVariable String playbackDeviceId,
			@RequestParam(required=false,defaultValue="date")  String order,
			@RequestParam(required=false,defaultValue="false")  boolean asc,
			@RequestParam(required=false,defaultValue="0") int start,
			@RequestParam(required=false,defaultValue="1") int max
			
		) throws EntityNotFoundException, DatabaseException, AccessDeniedException {
		return getService().getCinemaServerStatus(playbackDeviceId, order, asc, start, max);
	}
	
	@RequestMapping(value="/{playbackDeviceId}/status/{statusId}",method=RequestMethod.GET)
	@ResponseBody
	public FmsCinemaServerStatusBrief getPlaybackDeviceCredentials(
			@PathVariable String playbackDeviceId, 
			@PathVariable String statusId
		) throws EntityNotFoundException, DatabaseException, AccessDeniedException {
		return getService().getCinemaServerStatus(playbackDeviceId, statusId);
	}
	
	@RequestMapping(value="/{playbackDeviceId}/status/{statusId}/details",method=RequestMethod.GET)
	@ResponseBody
	public FmsCinemaServerStatus getStatusDetail(@PathVariable String playbackDeviceId, @PathVariable String statusId) throws EntityNotFoundException, DatabaseException, AccessDeniedException {
		return getService().getCinemaServerStatusDetail(playbackDeviceId, statusId);
	}
	
	
}
