package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.movie.FmsMovieVersion;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsMovieVersionService extends GenericService<FmsMovieVersion> {

	public List<FmsMovieVersion> listMovieVersions(String searchTerm, String movieId, String externalId, String mediaClipId, Integer statusBelow, Integer statusAbove, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException ;

}
