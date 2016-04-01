package com.openfms.core.service.project;

import java.util.List;

import com.cinefms.dbstore.api.DBStoreBinary;
import com.openfms.core.service.GenericService;
import com.openfms.model.core.movie.FmsFile;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsFileService extends GenericService<FmsFile> {

	public List<FmsFile> listFiles(String searchTerm, String type, String externalId, String quickhash, String packageId, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException;

	public void saveFileContents(DBStoreBinary binary) throws AccessDeniedException, DatabaseException;

	public DBStoreBinary getFileContents(String fileId) throws AccessDeniedException, DatabaseException;

}
