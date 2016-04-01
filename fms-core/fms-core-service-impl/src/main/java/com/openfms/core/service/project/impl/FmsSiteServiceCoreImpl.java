package com.openfms.core.service.project.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsSiteService;
import com.openfms.model.core.playback.FmsSite;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Service
public class FmsSiteServiceCoreImpl extends GenericProjectServiceImpl<FmsSite> implements FmsSiteService {

	@Override
	public List<FmsSite> listSites(String searchTerm, String externalId, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (searchTerm != null) {
			q = q.contains("name", searchTerm);
		}
		if (externalId != null) {
			q = q.eq("externalId", externalId);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		return list(q);
	}


}
