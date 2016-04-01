package com.openfms.core.service.project.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsFileCopyService;
import com.openfms.model.core.storage.FmsFileCopy;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Service
public class FmsFileCopyServiceCoreImpl extends GenericProjectServiceImpl<FmsFileCopy> implements FmsFileCopyService {

	@Override
	public List<FmsFileCopy> listFileCopies(String searchTerm, String packageId, String packageCopyId, String fileId, String lastUpdateBefore, String lastUpdateAfter, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (searchTerm != null) {
			q = q.contains("name", searchTerm);
		}
		if (packageId != null) {
			q = q.eq("packageId", packageId);
		}
		if (packageCopyId != null) {
			q = q.eq("packageCopyId", packageCopyId);
		}
		if (fileId != null) {
			q = q.eq("fileId", fileId);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		return list(q);
	}

}
