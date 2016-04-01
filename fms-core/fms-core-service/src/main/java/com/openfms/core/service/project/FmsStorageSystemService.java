package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.storage.FmsStorageSystem;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsStorageSystemService extends GenericService<FmsStorageSystem> {

	public List<FmsStorageSystem> listStorageSystems(String searchTerm, String externalId, String locationId, Boolean online, Boolean permanent, Integer status, Integer statusAbove, Integer statusBelow, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException;

}
