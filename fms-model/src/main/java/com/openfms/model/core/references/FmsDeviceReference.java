package com.openfms.model.core.references;

import java.util.ArrayList;
import java.util.List;

import com.openfms.model.core.playback.FmsPlaybackDevice;

public class FmsDeviceReference {

	private String deviceId;
	private String deviceName;
	private List<String> deviceTags;
	private String deviceVendor;
	private String deviceSerial;
	private String deviceModel;
	private String deviceType;
	private String certificateId;
	private String certificateDnQualifier;
	private List<String> mediaClipTypes;
	private boolean disabled;
	private List<String> tdlThumbprints;

	public FmsDeviceReference() {
	}
	
	public FmsDeviceReference(FmsPlaybackDevice device) {
		this.deviceId = device.getId();
		this.setTdlThumbprints(new ArrayList<String>());
		this.setCertificateDnQualifier(null);
		this.setCertificateId(null);
		if(device.getCertificate()!=null) {
			this.certificateId = device.getCertificateId();
			this.certificateDnQualifier = device.getCertificate().getDnQualifier();
			this.setTdlThumbprints(device.getCertificate().getTdlThumbprints());
		}
		this.deviceName = device.getName();
		this.deviceVendor = device.getVendor();
		this.deviceSerial = device.getSerial();
		this.deviceModel = device.getModel();
		this.deviceType = device.getDeviceType();
		this.setDeviceTags(device.getTags());
		this.setMediaClipTypes(device.getMediaClipTypes());
		this.setDisabled(device.isDisabled());
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public String getCertificateDnQualifier() {
		return certificateDnQualifier;
	}

	public void setCertificateDnQualifier(String certificateDnQualifier) {
		this.certificateDnQualifier = certificateDnQualifier;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceVendor() {
		return deviceVendor;
	}

	public void setDeviceVendor(String deviceVendor) {
		this.deviceVendor = deviceVendor;
	}

	public String getDeviceSerial() {
		return deviceSerial;
	}

	public void setDeviceSerial(String deviceSerial) {
		this.deviceSerial = deviceSerial;
	}

	public List<String> getDeviceTags() {
		return deviceTags;
	}

	public void setDeviceTags(List<String> deviceTags) {
		this.deviceTags = deviceTags;
	}

	public List<String> getMediaClipTypes() {
		return mediaClipTypes;
	}

	public void setMediaClipTypes(List<String> mediaClipTypes) {
		this.mediaClipTypes = mediaClipTypes;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public List<String> getTdlThumbprints() {
		return tdlThumbprints;
	}

	public void setTdlThumbprints(List<String> tdlThumbprints) {
		this.tdlThumbprints = tdlThumbprints;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

}
