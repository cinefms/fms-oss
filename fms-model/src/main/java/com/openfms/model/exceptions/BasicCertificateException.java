package com.openfms.model.exceptions;

public class BasicCertificateException extends CertificateException {

	private static final long serialVersionUID = -3557206884547093615L;

	public BasicCertificateException(Throwable cause) {
		super("BASIC_CERTIFICATE_EXCEPTION", cause);
	}

}
