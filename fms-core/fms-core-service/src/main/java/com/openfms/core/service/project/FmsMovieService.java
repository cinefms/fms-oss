package com.openfms.core.service.project;

import java.util.Date;
import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsMovieService extends GenericService<FmsMovie> {

	public List<FmsMovie> listMovies(List<String> ids, String movieExternalId, String category, Integer[] mediaStatus, Integer[] versionStatus, Integer[] encryptionStatus, Integer[] screeningStatus, Date dateFrom, Date dateTo, String categorySearch, Boolean openTasks, Boolean hasEvents, Boolean hasVersion, Boolean hasMediaClips, String searchTerm, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException;

}
