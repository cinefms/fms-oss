package com.openfms.core.service.project.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.model.core.movie.FmsMovieVersion;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Component
public class FmsMovieVersionServiceCoreImpl extends GenericProjectServiceImpl<FmsMovieVersion> implements FmsMovieVersionService {

	@Autowired
	private FmsMovieService movieService;

	@Override
	public List<FmsMovieVersion> listMovieVersions(String searchTerm, String movieId, String externalId, String mediaClipId, Integer statusBelow, Integer statusAbove, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (externalId != null) {
			q = q.eq("externalId", externalId);
		}
		if (movieId != null) {
			q = q.eq("movieId", movieId);
		}
		if (mediaClipId != null) {
			q = q.in("mediaClipIds", mediaClipId);
		}
		if (statusAbove != null) {
			q = q.gte("status", statusAbove);
		}
		if (statusBelow != null) {
			q = q.lte("status", statusBelow);
		}
		q = q.start(start == null ? 0 : start);
		if (max != null) {
			q = q.max(max == null ? -1 : max);
		}
		return list(q);
	}

}
