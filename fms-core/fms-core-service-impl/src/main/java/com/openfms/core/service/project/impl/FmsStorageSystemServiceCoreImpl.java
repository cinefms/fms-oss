package com.openfms.core.service.project.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsStorageSystemService;
import com.openfms.model.core.storage.FmsStorageSystem;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Service
public class FmsStorageSystemServiceCoreImpl extends GenericProjectServiceImpl<FmsStorageSystem> implements FmsStorageSystemService {

	@Override
	public List<FmsStorageSystem> listStorageSystems(String searchTerm, String externalId, String locationId, Boolean online, Boolean permanent, Integer status, Integer statusAbove, Integer statusBelow, String order, boolean asc, Integer start, Integer max)
			throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (searchTerm != null) {
			q = q.contains("name", searchTerm);
		}
		if (externalId != null) {
			q = q.eq("externalId", externalId);
		}
		if (locationId != null) {
			q = q.eq("locationId", locationId);
		}
		if (online != null) {
			q = q.eq("online", online);
		}
		if (permanent != null) {
			q = q.eq("permanent", permanent);
		}
		if (status != null) {
			q = q.eq("status", status);
		}
		if (statusAbove != null) {
			q = q.gte("statusAbove", statusAbove);
		}
		if (statusBelow != null) {
			q = q.lte("statusBelow", statusBelow);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		return list(q);
	}


}
