package com.openfms.core.service.project;

import java.util.List;

import com.openfms.model.core.movie.FmsMovie;
import com.openfms.model.core.movie.ImportResult;
import com.openfms.model.core.playback.FmsEvent;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.core.playback.FmsSite;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.ConcurrencyException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;

public interface ImportService {

	public List<ImportResult> importMovies(List<FmsMovie> movies, boolean deleteObsolete) throws DatabaseException, EntityNotFoundException, AccessDeniedException, ConcurrencyException;

	public List<ImportResult> importEvents(List<FmsEvent> events, boolean deleteObsolete) throws DatabaseException, EntityNotFoundException, InvalidParameterException, AccessDeniedException, ConcurrencyException;

	public List<ImportResult> importSites(List<FmsSite> sites, boolean deleteObsolete) throws DatabaseException, EntityNotFoundException, AccessDeniedException;

	public List<ImportResult> importLocations(List<FmsLocation> locations, boolean deleteObsolete) throws DatabaseException, EntityNotFoundException, AccessDeniedException;

}
