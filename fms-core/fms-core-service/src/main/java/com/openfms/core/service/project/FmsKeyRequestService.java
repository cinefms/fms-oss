package com.openfms.core.service.project;

import java.io.IOException;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.crypto.FmsKeyRequest;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

public interface FmsKeyRequestService extends GenericService<FmsKeyRequest> {

	public void fulfillKeyRequest(String id, byte[] key) throws AccessDeniedException, DatabaseException, EntityNotFoundException, IOException, VersioningException;

	
	
	
	
}
