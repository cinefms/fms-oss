package com.openfms.model.exceptions;


public class CertificateChainException extends CertificateException {

	private static final long serialVersionUID = 1180499165567615260L;

	public CertificateChainException(Throwable cause, Object[] params) {
		super("CERTIFICATE_CHAIN_INCONSISTENT", cause, params);
	}

}
