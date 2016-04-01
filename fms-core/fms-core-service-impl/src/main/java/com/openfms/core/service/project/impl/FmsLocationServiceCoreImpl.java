package com.openfms.core.service.project.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsLocationService;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Service
public class FmsLocationServiceCoreImpl extends GenericProjectServiceImpl<FmsLocation> implements FmsLocationService {

	@Override
	public List<FmsLocation> listLocations(String searchTerm, String[] siteIds, String[] locationIds, String externalId, Boolean active, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (externalId != null) {
			q = q.eq("externalId", externalId);
		}
		if (siteIds != null) {
			q = q.in("siteId", siteIds);
		}
		if (active != null) {
			q = q.eq("active", active.booleanValue());
		}
		if (locationIds != null) {
			List<String> locationIdList = new ArrayList<String>();
			for (String s : locationIds) {
				locationIdList.add(s);
			}
			q = q.in("_id", locationIdList);
		}
		if (searchTerm != null) {
			DBStoreQuery qname = BasicQuery.createQuery().contains("name", searchTerm);
			DBStoreQuery qeid = BasicQuery.createQuery().contains("externalId", searchTerm);
			q = q.or(qname, qeid);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		return list(q);
	}

}
