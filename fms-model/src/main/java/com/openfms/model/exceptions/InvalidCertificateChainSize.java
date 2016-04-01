package com.openfms.model.exceptions;

public class InvalidCertificateChainSize extends CertificateException {

	private static final long serialVersionUID = 611243543003155207L;

	public InvalidCertificateChainSize(int should, int is) {
		super("INVALID_CERTIFICATE_CHAIN_SIZE", null, should, is);
	}

}
