package com.openfms.core.api.v2.controllers.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.api.v2.utils.DateRange;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.movie.FmsMediaClipData;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(MediaClipController.BASE_URI)
public class MediaClipController extends GenericController<FmsMediaClip> {

	public static final String BASE_URI = "/mediaclips/clips";
	
	@Autowired
	private FmsMediaClipService mediaClipService;
	
	@Override
	protected GenericService<FmsMediaClip> getService() {
		return mediaClipService;
	}
	
	@Override
	protected Class<FmsMediaClip> getType() {
		return FmsMediaClip.class;
	}

	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMediaClip> list(
			@RequestParam(required=false) String movieId, 
			@RequestParam(required=false) String movieCategory, 
			@RequestParam(required=false, value="type") String[] types, 
			@RequestParam(required=false,value="tag") String[] tags,
			@RequestParam(required=false) String externalId, 
			@RequestParam(required=false) String eventId, 
			@RequestParam(required=false) Integer[] status, 
			@RequestParam(required=false) String moviePackageId, 
			@RequestParam(required=false) DateRange screeningWithin,
			@RequestParam(required=false) Boolean disabled, 
			@RequestParam(required=false) Boolean encrypted,
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false) String order, boolean asc, 
			@RequestParam(required=false) Integer start, 
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		
		DBStoreQuery q = BasicQuery.createQuery();
		
		if (eventId != null) {
			q = q.in("events.eventId", eventId);
		}

		if (moviePackageId != null) {
			q = q.in("moviePackageIds", moviePackageId);
		}
		if (movieId != null) {
			q = q.eq("movieId", movieId);
		}
		if (movieCategory != null) {
			q = q.eq("movieCategory", movieCategory);
		}
		if (disabled != null) {
			q = q.eq("disabled", disabled.booleanValue());
		}
		if (encrypted != null) {
			q = q.eq("encrypted", encrypted.booleanValue());
		}
		if (searchTerm != null) {
			q = q.contains("searchable", searchTerm);
		}
		if (externalId != null) {
			q = q.eq("externalId", externalId);
		}
		if (types != null && types.length>0) {
			q = q.in("type", types);
		}
		if (tags != null && tags.length>0) {
			q = q.in("tags", tags);
		}
		if (status != null) {
			q = q.in("status", status);
		}
		if (screeningWithin != null) {
			if(screeningWithin.getFrom()!=null) {
				q = q.gte("events.screeningDate", screeningWithin.getFrom());
			}
			if(screeningWithin.getTo()!=null) {
				q = q.lte("events.screeningDate", screeningWithin.getTo());
			}
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max);
		return mediaClipService.list(q);
	}


	@RequestMapping(value="/{mediaClipId}/data",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsMediaClipData> listMediaClipData(@PathVariable String mediaClipsId) {
		return null;
	}

	
}
