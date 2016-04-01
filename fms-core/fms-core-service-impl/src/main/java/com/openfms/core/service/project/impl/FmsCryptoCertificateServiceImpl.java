package com.openfms.core.service.project.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.api.DBStoreBinary;
import com.cinefms.dbstore.api.impl.BasicBinary;
import com.openfms.core.service.project.FmsCryptoCertificateService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.crypto.FmsCertificate;
import com.openfms.model.core.crypto.FmsCertificateData;
import com.openfms.model.core.crypto.FmsCertificateParseResult;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.CertificateException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;
import com.openfms.model.spi.crypto.CertParser;

@Service
public class FmsCryptoCertificateServiceImpl extends GenericProjectServiceImpl<FmsCertificate> implements FmsCryptoCertificateService {

	private static Log log = LogFactory.getLog(FmsCryptoCertificateServiceImpl.class);
	
	@Autowired
	private CertParser certificateParser;
	
	private static final String CERTIFICATE_DATA_BUCKET = "CertificateData";

	@Override
	public DBStoreBinary getData(String certificateId) throws EntityNotFoundException, DatabaseException, InvalidParameterException, AccessDeniedException {
		if(!authzService.allowAccess(FmsCertificateData.class, certificateId, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		try {
			return dataStore.getBinary(CERTIFICATE_DATA_BUCKET, certificateId);
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void setData(DBStoreBinary data) throws EntityNotFoundException, DatabaseException, InvalidParameterException, AccessDeniedException {
		if(!authzService.allowAccess(FmsCertificateData.class, data.getId(), AccessType.UPDATE)) {
			throw new AccessDeniedException();
		}
		try {
			FmsCertificate c = dataStore.getObject(FmsCertificate.class, data.getId());
			dataStore.saveBinary(data, CERTIFICATE_DATA_BUCKET);
			DBStoreBinary b = dataStore.getBinary(CERTIFICATE_DATA_BUCKET, data.getId());
			c.setSize((int)b.getLength());
			if(c.getSize()<1) {
				throw new RuntimeException("saved binary was 0 bytes or smaller - this is wrong!");
			}
			dataStore.saveObject(c);
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	
	}

	@Override
	public List<FmsCertificateParseResult> upload(byte[] bytes) throws CertificateException {
		log.info("CERTIFICATE UPLOAD: "+bytes.length+" bytes");
		List<FmsCertificateParseResult> res = certificateParser.parseCerts(bytes); 
		log.info("CERTIFICATE UPLOAD: "+bytes.length+" bytes, "+res.size()+" files ... ");
		for(FmsCertificateParseResult r : res) {
			if(r.isOk()) {
				log.info("CERTIFICATE UPLOAD: OK - "+r.getCertificate().getCertificateDnQualifier());
				try {
					
					if(r.getData()==null || r.getData().length<1) {
						throw new RuntimeException("unable to save certificate! zero length file!");
					}
					
					log.info("CERTIFICATE UPLOAD: OK - "+r.getCertificate().getCertificateDnQualifier()+" saving CERT ");
					FmsCertificate c = r.getCertificate();
					c.setSize(r.getData().length);
					c = save(c);
					
					if(c.getId()==null) {
						throw new RuntimeException("unable to save certificate meta data!");
					}
					
					log.info("CERTIFICATE UPLOAD: OK - "+r.getCertificate().getCertificateDnQualifier()+" saving CERT DATA");
					DBStoreBinary b = new BasicBinary(c.getId(),r.getData());

					dataStore.saveBinary(b, CERTIFICATE_DATA_BUCKET);

					r.setCertificate(c);
					
				} catch (Exception e) {
					e.printStackTrace();
					r.setOk(false);
				}
			} else {
				log.info("CERTIFICATE UPLOAD: NOT OK - "+r.getCertificate().getCertificateDnQualifier());
			}
		}
		return res;
	}
	
}
