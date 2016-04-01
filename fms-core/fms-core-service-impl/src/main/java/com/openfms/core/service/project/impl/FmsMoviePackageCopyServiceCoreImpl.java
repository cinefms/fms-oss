package com.openfms.core.service.project.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsMoviePackageCopyService;
import com.openfms.model.core.storage.FmsMoviePackageCopy;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Service
public class FmsMoviePackageCopyServiceCoreImpl extends GenericProjectServiceImpl<FmsMoviePackageCopy> implements FmsMoviePackageCopyService {

	@Override
	public List<FmsMoviePackageCopy> listMoviePackageCopies(String searchTerm, String moviePackageId, String externalId, Integer status, Integer statusAbove, Integer statusBelow, String storageSystemId, String order, boolean asc, Integer start, Integer max)
			throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (searchTerm != null) {
			q = q.contains("searchTerm", searchTerm);
		}
		if (moviePackageId != null) {
			q = q.eq("moviePackageId", moviePackageId);
		}
		if (externalId != null) {
			q = q.eq("externalId", externalId);
		}
		if (status != null) {
			q = q.eq("status", status);
		}
		if (statusAbove != null) {
			q = q.gte("status", statusAbove);
		}
		if (statusBelow != null) {
			q = q.lte("status", statusBelow);
		}
		if (storageSystemId != null) {
			q = q.eq("storageSystemId", storageSystemId);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		return list(q);
	}

}
