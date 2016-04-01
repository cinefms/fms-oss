package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.movie.FmsMoviePackage;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsMoviePackageService extends GenericService<FmsMoviePackage> {

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
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException;

}
