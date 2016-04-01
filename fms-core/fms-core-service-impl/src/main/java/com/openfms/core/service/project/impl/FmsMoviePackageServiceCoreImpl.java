package com.openfms.core.service.project.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsFileService;
import com.openfms.core.service.project.FmsMoviePackageCopyService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.model.core.movie.FmsMoviePackage;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Component
public class FmsMoviePackageServiceCoreImpl extends GenericProjectServiceImpl<FmsMoviePackage> implements FmsMoviePackageService {

	@Autowired
	private FmsFileService fileService;

	@Autowired
	private FmsMoviePackageCopyService moviePackageCopyService;

	@Autowired
	private FmsMovieVersionService movieVersionService;

	@Override
	public List<FmsMoviePackage> listMoviePackages(
			String searchTerm, 
			String movieId, 
			String movieVersionId,
			String eventId,
			String type,
			String externalId, 
			String quickhash, 
			String order, 
			boolean asc, 
			Integer start, 
			Integer max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (searchTerm != null) {
			q = q.contains("name", searchTerm);
		}
		if (movieId != null) {
			q = q.eq("movieIds", movieId);
		}
		if (movieVersionId != null) {
			q = q.eq("movieVersionIds", movieVersionId);
		}
		if (externalId != null) {
			q = q.eq("externalId", externalId);
		}
		if (eventId != null) {
			q = q.eq("events.eventId", eventId);
		}
		if (quickhash != null) {
			q = q.eq("quickhash", quickhash);
		}
		if (type != null) {
			q = q.eq("type", type);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		return list(q);
	}

}
