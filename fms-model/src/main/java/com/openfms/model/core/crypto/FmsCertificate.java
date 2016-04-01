package com.openfms.model.core.crypto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;
import com.openfms.model.core.references.FmsCertificateReference;
import com.openfms.model.core.references.FmsDeviceReference;


@FmsAccessControlled(FmsAccessControlled.GROUP.CERTIFICATES)
public class FmsCertificate extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	public enum TYPE { ROOT, SIGNER, DEVICE };
	
	private String certificateDn;
	private String certificateDnQualifier;
	private String certificateUuid;

	private String playbackDeviceName;
	private String playbackDeviceId;
	
	private TYPE type;

	private String parentDn;
	private String parentDnQualifier;
	private String parentId;

	private String serial;
	private Date validFrom;
	private Date validTo;
	
	private int size;

	private String category;
	
	private List<FmsDeviceReference> devices = new ArrayList<FmsDeviceReference>();
	private List<String> trustedCertificateIds = new ArrayList<String>();
	private List<String> tdlThumbprints = new ArrayList<String>();
	
	private List<FmsCertificateReference> trustedCertificates = new ArrayList<FmsCertificateReference>();  
	
	
	public FmsCertificate() {
		super(null);
	}

	public FmsCertificate(String id) {
		super(id);
	}
	
	@Override
	public String getName() {
		String s = super.getName();
		if(s == null || s.length()==0) {
			return getCertificateDn();
		}
		return s;
	}

	public String getCertificateDn() {
		return certificateDn;
	}

	public void setCertificateDn(String certificateDn) {
		this.certificateDn = certificateDn;
	}

	public String getCertificateUuid() {
		return certificateUuid;
	}

	public void setCertificateUuid(String ceritficateUuid) {
		this.certificateUuid = ceritficateUuid;
	}

	public String getParentDn() {
		return parentDn;
	}

	public void setParentDn(String parentDn) {
		this.parentDn = parentDn;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getCertificateDnQualifier() {
		return certificateDnQualifier;
	}

	public void setCertificateDnQualifier(String certificateDnQualifier) {
		this.certificateDnQualifier = certificateDnQualifier;
	}

	public String getParentDnQualifier() {
		return parentDnQualifier;
	}

	public void setParentDnQualifier(String parentDnQualifier) {
		this.parentDnQualifier = parentDnQualifier;
	}

	public String getPlaybackDeviceName() {
		return playbackDeviceName;
	}

	public void setPlaybackDeviceName(String playbackDeviceName) {
		this.playbackDeviceName = playbackDeviceName;
	}

	public String getPlaybackDeviceId() {
		return playbackDeviceId;
	}

	public void setPlaybackDeviceId(String playbackDeviceId) {
		this.playbackDeviceId = playbackDeviceId;
	}

	public boolean isRoot() {
		return certificateDn.compareTo(parentDn)==0;
	}

	public void setRoot(boolean root) {
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<FmsDeviceReference> getDevices() {
		return devices;
	}

	public void setDevices(List<FmsDeviceReference> devices) {
		this.devices = devices;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public List<String> getTrustedCertificateIds() {
		return trustedCertificateIds;
	}

	public void setTrustedCertificateIds(List<String> trustedCertificateIds) {
		this.trustedCertificateIds = trustedCertificateIds;
	}

	public List<FmsCertificateReference> getTrustedCertificates() {
		return trustedCertificates;
	}

	public void setTrustedCertificates(List<FmsCertificateReference> trustedCertificates) {
		this.trustedCertificates = trustedCertificates;
	}

	public List<String> getTdlThumbprints() {
		if(tdlThumbprints==null) {
			tdlThumbprints = new ArrayList<String>();
		}
		return tdlThumbprints;
	}

	public void setTdlThumbprints(List<String> tdlThumbprints) {
		this.tdlThumbprints = tdlThumbprints;
	}

	public boolean matchesCertificate(FmsKey key) {
		return key!=null && key.getCertificateDn()!=null && this.getCertificateDn()!=null && this.getCertificateDn().compareTo(key.getCertificateDn())==0;
	}
	
	public boolean matchesTDL(FmsKey key) {
		if(
				(key.getTdlThumbprints()==null || key.getTdlThumbprints().size()==0) &&
				(getTdlThumbprints()==null || getTdlThumbprints().size()==0)
		) {
			return true;
		}
		
		for(String ctdl : getTdlThumbprints()) {
			if(!key.getTdlThumbprints().contains(ctdl)) {
				return false;
			}
		}
		return true;
	}
	

	
}
