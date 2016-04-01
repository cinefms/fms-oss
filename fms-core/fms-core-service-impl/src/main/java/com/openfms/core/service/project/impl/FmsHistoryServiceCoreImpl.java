package com.openfms.core.service.project.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsHistoryService;
import com.openfms.model.core.auth.FmsHistory;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Service
public class FmsHistoryServiceCoreImpl implements FmsHistoryService {
	
	@Autowired
	private ProjectDataStore projectDataStore;
	

	@Override
	public List<FmsHistory> listHistory(String modifiedObjectId, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (modifiedObjectId != null) {
			q = q.eq("modifiedObjectId", modifiedObjectId);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		try {
			List<FmsHistory> out = projectDataStore.findObjects(FmsHistory.class, q);
			return out;
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}
	

}
