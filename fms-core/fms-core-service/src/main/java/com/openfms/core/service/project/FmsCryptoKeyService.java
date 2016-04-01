package com.openfms.core.service.project;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.cinefms.dbstore.api.DBStoreBinary;
import com.openfms.core.service.GenericService;
import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.crypto.FmsKeyParseResult;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;

public interface FmsCryptoKeyService extends GenericService<FmsKey> {
	
	public List<FmsKey> listKeys(
			String searchTerm, String externalId, 
			String keyId, String movieId, String movieVersionId, 
			String mediaClipId, String mediaClipExternalId, 
			String issuerDnQualifier, 
			String certificateDnQualifier, 
			String certificateId, 
			String deviceId, 
			String eventId, Date validAt, 
			String order, 
			boolean asc, int start, int max) throws DatabaseException, EntityNotFoundException, AccessDeniedException ;

	public DBStoreBinary getData(String keyId) throws EntityNotFoundException, DatabaseException, InvalidParameterException, AccessDeniedException;
	
	public void setData(DBStoreBinary data) throws EntityNotFoundException, DatabaseException, InvalidParameterException, AccessDeniedException;

	public List<FmsKeyParseResult> upload(String movieId, String filename, byte[] bytes, String source) throws IOException, AccessDeniedException;
	


}
