package com.openfms.core.service.project.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinefms.dbstore.api.DBStoreBinary;
import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsFileService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.movie.FmsFile;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Service
public class FmsFileServiceCoreImpl extends GenericProjectServiceImpl<FmsFile> implements FmsFileService {

	@Override
	public List<FmsFile> listFiles(String searchTerm, String type, String externalId, String quickhash, String packageId, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (externalId != null) {
			q = q.eq("externalId", externalId);
		}
		if (packageId != null) {
			q = q.eq("packageId", packageId);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		return list(q);
	}

	@Override
	public void saveFileContents(DBStoreBinary b) throws AccessDeniedException, DatabaseException {
		if(!authzService.allowAccess(FmsFile.class, b.getId(), AccessType.UPDATE)) {
			throw new AccessDeniedException();
		}
		try {
			dataStore.saveBinary(b, "FileContents");
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
		
	}

	@Override
	public DBStoreBinary getFileContents(String fileId) throws AccessDeniedException, DatabaseException {
		if(!authzService.allowAccess(FmsFile.class, fileId, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		try {
			return dataStore.getBinary("FileContents", fileId);
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}

	

	
	
	
	
}
