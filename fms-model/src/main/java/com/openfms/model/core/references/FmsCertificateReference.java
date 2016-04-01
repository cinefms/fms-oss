package com.openfms.model.core.references;

import java.util.List;

import com.openfms.model.core.crypto.FmsCertificate;

public class FmsCertificateReference {

	private String certificateId;
	private String certificateDn;
	private String dnQualifier;
	private List<String> tdlThumbprints;

	public FmsCertificateReference() {
	}
	
	public FmsCertificateReference(FmsCertificate certificate) {
		this.certificateId = certificate.getId();
		this.dnQualifier = certificate.getCertificateDnQualifier();
		this.tdlThumbprints = certificate.getTdlThumbprints();
		this.certificateDn = certificate.getCertificateDn();
	}
	
	public String getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public String getDnQualifier() {
		return dnQualifier;
	}

	public void setDnQualifier(String dnQualifier) {
		this.dnQualifier = dnQualifier;
	}

	public List<String> getTdlThumbprints() {
		return tdlThumbprints;
	}

	public void setTdlThumbprints(List<String> tdlThumbprints) {
		this.tdlThumbprints = tdlThumbprints;
	}

	public String getCertificateDn() {
		return certificateDn;
	}

	public void setCertificateDn(String certificateDn) {
		this.certificateDn = certificateDn;
	}

}
