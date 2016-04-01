package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.playback.FmsSite;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsSiteService extends GenericService<FmsSite> {

	
	public List<FmsSite> listSites(String searchTerm, String externalId, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException ;


}
