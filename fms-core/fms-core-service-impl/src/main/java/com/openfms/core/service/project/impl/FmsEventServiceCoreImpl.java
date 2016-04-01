package com.openfms.core.service.project.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsEventPBS;
import com.openfms.model.core.playback.FmsEventPlaybackStatusBrief;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;
import com.openfms.model.exceptions.VersioningException;

@Service
public class FmsEventServiceCoreImpl extends GenericProjectServiceImpl<FmsEvent> implements FmsEventService {

	private static Log log = LogFactory.getLog(FmsEventServiceCoreImpl.class);

	@Override
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
			Integer start, Integer max) throws DatabaseException, EntityNotFoundException, InvalidParameterException, AccessDeniedException {

		DBStoreQuery q = BasicQuery.createQuery();
		if (searchTerm != null) {
			q = q.contains("searchable", searchTerm);
		}
		if (externalId != null) {
			q = q.eq("externalId", externalId);
		}
		if (locationIds != null) {
			List<DBStoreQuery> lIds = new ArrayList<DBStoreQuery>();
			for (String lId : locationIds) {
				lIds.add(BasicQuery.createQuery().eq("locationId", lId));
			}
			q = q.or(lIds);
		}
		if (category != null) {
			q = q.eq("category", category);
		}
		if (tags != null) {
			q = q.all("tags", tags);
		}

		if (statusAbove != null) {
			q = q.gte("status", statusAbove);
		}
		if (statusBelow != null) {
			q = q.lte("status", statusBelow);
		}
		if (status != null) {
			q = q.in("status", status);
		}

		if (versionStatus != null) {
			q = q.in("versionStatus", versionStatus);
		}
		if (mediaStatus != null) {
			q = q.in("mediaStatus", mediaStatus);
		}
		if (encryptionStatus != null) {
			q = q.in("encryptionStatus", encryptionStatus);
		}
		if (playbackStatus != null) {
			q = q.in("playbackStatus", playbackStatus);
		}

		if (movieId != null) {
			q = q.eq("eventItems.movieId", movieId);
		}
		if (movieVersionId != null) {
			q = q.eq("eventItems.movieVersionId", movieVersionId);
		}
		if (mediaClipType != null) {
			q = q.eq("eventItems.mediaClips.mediaClipType", mediaClipType);
		}
		if (mediaClipId != null) {
			q = q.in("eventItems.mediaClips.mediaClipId", mediaClipId);
		}
		if (startBefore != null) {
			q = q.lte("startTime", startBefore);
		}
		if (startAfter != null) {
			q = q.gte("startTime", startAfter);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		
		log.info(" --- "+q.toString());
		
		return list(q);
	}

	@Override
	public FmsEventPlaybackStatusBrief getEventStatus(String eventId, String statusId) throws DatabaseException, AccessDeniedException{
		if(!authzService.allowAccess(FmsEvent.class, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		DBStoreQuery q = BasicQuery.createQuery().eq("eventId", eventId).eq("_id", statusId);
		FmsEventPBS s = dataStore.findObject(FmsEventPBS.class, q);
		if(s == null) {
			return null;
		}
		return s.getBrief();
	}

	@Override
	public FmsEventPBS getEventStatusDetail(String eventId, String statusId)  throws DatabaseException, AccessDeniedException{
		if(!authzService.allowAccess(FmsEvent.class, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		DBStoreQuery q = BasicQuery.createQuery().eq("eventId", eventId).eq("_id", statusId);
		return dataStore.findObject(FmsEventPBS.class, q);
	}

	@Override
	public List<FmsEventPlaybackStatusBrief> listEventStatus(String eventId, String order, boolean asc, int start, int max)  throws DatabaseException, AccessDeniedException{
		if(!authzService.allowAccess(FmsEvent.class, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		DBStoreQuery q = BasicQuery.createQuery().eq("eventId", eventId).order(order,asc).start(start).max(max);
		List<FmsEventPlaybackStatusBrief> out = new ArrayList<FmsEventPlaybackStatusBrief>();
		for(FmsEventPBS s : dataStore.findObjects(FmsEventPBS.class, q)) {
			out.add(s.getBrief());
		}
		return out;
	}

	@Override
	public FmsEventPBS saveEventStatus(String eventId, FmsEventPBS status) throws DatabaseException, VersioningException, AccessDeniedException {
		if(!authzService.allowAccess(FmsEvent.class, AccessType.CREATE)) {
			throw new AccessDeniedException();
		}
		status.setEventId(eventId);
		return dataStore.saveObject(status);
	}


}
