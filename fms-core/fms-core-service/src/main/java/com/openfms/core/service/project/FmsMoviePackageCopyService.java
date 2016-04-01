package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.storage.FmsMoviePackageCopy;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsMoviePackageCopyService extends GenericService<FmsMoviePackageCopy> {

	public List<FmsMoviePackageCopy> listMoviePackageCopies(String search, String packageId, String externalId, Integer status, Integer statusAbove, Integer statusBelow, String storageSystemId, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException;


}
