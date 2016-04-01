package com.openfms.core.service.project;

import java.util.Date;
import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsEventPBS;
import com.openfms.model.core.playback.FmsEventPlaybackStatusBrief;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;
import com.openfms.model.exceptions.VersioningException;

public interface FmsEventService extends GenericService<FmsEvent> {


	public List<FmsEvent> listEvents(
			String searchTerm, 
			String externalId, 
			String[] locationIds,
			String[] tags,
			String category,
			Integer[] status,
			Integer statusAbove,
			Integer statusBelow,
			Integer[] mediaStatus,
			Integer[] versionStatus,
			Integer[] encryptionStatus,
			Integer[] playbackStatus,
			String movieId,
			String movieVersionId, 
			String mediaClipId, 
			String mediaClipType, 
			Date startBefore, 
			Date startAfter, 
			String order, 
			boolean asc, 
			Integer start, Integer max
	) throws DatabaseException, EntityNotFoundException, InvalidParameterException, AccessDeniedException;

	public FmsEventPBS saveEventStatus(String eventId, FmsEventPBS status) throws DatabaseException, VersioningException, AccessDeniedException;

	public List<FmsEventPlaybackStatusBrief> listEventStatus(String eventId, String order, boolean asc, int start, int max) throws DatabaseException, AccessDeniedException;

	public FmsEventPlaybackStatusBrief getEventStatus(String eventId, String statusId) throws DatabaseException, AccessDeniedException;

	public FmsEventPBS getEventStatusDetail(String eventId, String statusId) throws DatabaseException, AccessDeniedException;


}
