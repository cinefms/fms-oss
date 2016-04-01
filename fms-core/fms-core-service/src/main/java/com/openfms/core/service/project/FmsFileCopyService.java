package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.storage.FmsFileCopy;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsFileCopyService extends GenericService<FmsFileCopy> {

	public List<FmsFileCopy> listFileCopies(
			String searchTerm, 
			String packageId,
			String packageCopyId,
			String fileId, 
			String lastUpdateBefore, 
			String lastUpdateAfter, 
			String order, 
			boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException;

}
