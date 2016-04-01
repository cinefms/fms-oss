package com.openfms.core.service.project;

import java.util.List;

import com.cinefms.dbstore.api.DBStoreBinary;
import com.openfms.core.service.GenericService;
import com.openfms.model.core.crypto.FmsCertificate;
import com.openfms.model.core.crypto.FmsCertificateParseResult;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.CertificateException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;

public interface FmsCryptoCertificateService extends GenericService<FmsCertificate> {
	
	
	public DBStoreBinary getData(String certificateId) throws EntityNotFoundException, DatabaseException, InvalidParameterException, AccessDeniedException;
	
	public void setData(DBStoreBinary data) throws EntityNotFoundException, DatabaseException, InvalidParameterException, AccessDeniedException;

	public List<FmsCertificateParseResult> upload(byte[] bytes) throws CertificateException;


}
