package com.openfms.core.service.project.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsMediaClipTaskTypeService;
import com.openfms.model.core.movie.FmsMediaClipTaskType;
import com.openfms.model.core.movie.FmsMediaClipTaskTypeNext;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Service
public class FmsMediaClipTaskTypeServiceImpl extends GenericProjectServiceImpl<FmsMediaClipTaskType> implements FmsMediaClipTaskTypeService {

	@Override
	public List<FmsMediaClipTaskType> list(String searchTerm, String previousTaskType, String mediaClipType, Boolean mandatory, String order, boolean asc, Integer start, Integer max) throws AccessDeniedException, DatabaseException, EntityNotFoundException {
		
		List<String> limit = null;
		if(previousTaskType!=null) {
			FmsMediaClipTaskType p = get(previousTaskType);
			limit = new ArrayList<String>();
			for(FmsMediaClipTaskTypeNext n : p.getNext()) {
				if(n.getNextTaskType()!=null && (mandatory==null || mandatory.booleanValue() == n.isDef())) {
					limit.add(n.getNextTaskType());
				}
			}
		}
		
 		
		DBStoreQuery q = BasicQuery.createQuery();
		if(searchTerm!=null) {
			q = q.contains("name", searchTerm);
		}
		if(limit!=null) {
			q = q.in("_id", limit);
		}
		if(mediaClipType!=null && mediaClipType.length()>0) {
			q = q.eq("enableForMediaClipTypes", mediaClipType);
		}
		q = q.order(order!=null?order:"name");
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		return list(q);
	}
	
}
