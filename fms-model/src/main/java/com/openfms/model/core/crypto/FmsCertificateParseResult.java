package com.openfms.model.core.crypto;

public class FmsCertificateParseResult {

	private boolean ok;
	private byte[] data;
	private FmsCertificate certificate;
	private String dnQualifier;
	
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public FmsCertificate getCertificate() {
		return certificate;
	}
	public void setCertificate(FmsCertificate certificate) {
		this.certificate = certificate;
	}
	public String getDnQualifier() {
		return dnQualifier;
	}
	public void setDnQualifier(String dnQualifier) {
		this.dnQualifier = dnQualifier;
	}
	
}
