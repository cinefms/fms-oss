package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.playback.FmsLocation;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsLocationService extends GenericService<FmsLocation> {

	
	public List<FmsLocation> listLocations(
			String searchTerm, 
			String[] siteIds, 
			String[] locationIds, 
			String externalId,
			Boolean active, 
			String order, 
			boolean asc, 
			Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException ;


}
