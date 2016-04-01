package com.openfms.model.spi.crypto;

import java.util.List;

import com.openfms.model.core.crypto.FmsCertificateParseResult;
import com.openfms.model.exceptions.BasicCertificateException;
import com.openfms.model.exceptions.CertificateException;

public interface CertParser {

	public List<FmsCertificateParseResult> parseCerts(byte[] data) throws CertificateException;

	public byte[] createCertificateChain(List<byte[]> certificateData) throws CertificateException;

	public List<FmsCertificateParseResult> parseChain(byte[] bytes) throws BasicCertificateException;

	
}
