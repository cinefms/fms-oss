package com.openfms.core.api.v2.controllers.project;

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

import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.api.v2.utils.DateRange;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsEventPBS;
import com.openfms.model.core.playback.FmsEventPlaybackStatusBrief;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(PlaybackEventController.BASE_URI)
public class PlaybackEventController extends GenericController<FmsEvent> {

	public static final String BASE_URI = "/playback/events";

	@Autowired
	private FmsEventService eventService;

	@Override
	protected GenericService<FmsEvent> getService() {
		return eventService;
	}

	@Override
	protected Class<FmsEvent> getType() {
		return FmsEvent.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsEvent> listEvents(
			@RequestParam(required=false, defaultValue="false") boolean refresh,
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false) String externalId, 
			@RequestParam(required=false,value="locationId") String[] locationIds, 
			@RequestParam(required=false,value="tag") String[] tags,
			@RequestParam(required=false) String category,
			@RequestParam(required=false) Integer[] status,
			@RequestParam(required=false) Integer statusAbove,
			@RequestParam(required=false) Integer statusBelow,
			@RequestParam(required=false,value="mediaStatus") Integer[] mediaStatus,
			@RequestParam(required=false,value="versionStatus") Integer[] versionStatus,
			@RequestParam(required=false,value="encryptionStatus") Integer[] encryptionStatus,
			@RequestParam(required=false,value="playbackStatus") Integer[] playbackStatus,
			@RequestParam(required=false) String movieId,
			@RequestParam(required=false) String movieVersionId,
			@RequestParam(required=false) String mediaClipId,
			@RequestParam(required=false) String mediaClipType, 
			@RequestParam(required=false) Date startBefore,
			@RequestParam(required=false) Date startAfter,
			@RequestParam(required=false) DateRange within,
			@RequestParam(required=false, defaultValue="name") String order,
			@RequestParam(required=false, defaultValue="true") boolean asc,
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, InvalidParameterException, AccessDeniedException {
		if(within!=null) {
			startBefore = within.getTo();
			startAfter = within.getFrom();
		}
		return eventService.listEvents(searchTerm,externalId,locationIds,tags,category,status,statusAbove,statusBelow,mediaStatus,versionStatus,encryptionStatus,playbackStatus,movieId,movieVersionId, mediaClipId, mediaClipType, startBefore, startAfter, order, asc, start, max);
	}
	
	@RequestMapping(value="/{eventId}/status",method=RequestMethod.POST)
	@ResponseBody
	public FmsEventPBS savePlaybackDeviceStatus(@PathVariable String eventId, @RequestBody FmsEventPBS status) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		status.setEventId(eventId);
		status.setDate(new Date());
		return eventService.saveEventStatus(eventId,status);
	}
	
	@RequestMapping(value="/{eventId}/status",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsEventPlaybackStatusBrief> listEventStatus(
			@PathVariable String eventId,
			@RequestParam(required=false,defaultValue="date")  String order,
			@RequestParam(required=false,defaultValue="false")  boolean asc,
			@RequestParam(required=false,defaultValue="0") int start,
			@RequestParam(required=false,defaultValue="1") int max
			
		) throws EntityNotFoundException, DatabaseException, AccessDeniedException {
		return eventService.listEventStatus(eventId, order, asc, start, max);
	}
	
	@RequestMapping(value="/{eventId}/status/{statusId}",method=RequestMethod.GET)
	@ResponseBody
	public FmsEventPlaybackStatusBrief getPlaybackDeviceStatus(
			@PathVariable String playbackDeviceId, 
			@PathVariable String statusId
		) throws EntityNotFoundException, DatabaseException, AccessDeniedException {
		return eventService.getEventStatus(playbackDeviceId, statusId);
	}
	
	@RequestMapping(value="/{eventId}/status/{statusId}/details",method=RequestMethod.GET)
	@ResponseBody
	public FmsEventPBS getStatusDetail(@PathVariable String eventId, @PathVariable String statusId) throws EntityNotFoundException, DatabaseException, AccessDeniedException {
		return eventService.getEventStatusDetail(eventId, statusId);
	}
	
	
	
}
