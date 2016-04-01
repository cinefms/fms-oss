package com.openfms.model.core.playback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.annotations.Searchable;
import com.openfms.model.core.AbstractFmsObject;
import com.openfms.model.core.references.FmsCertificateReference;

@Indexes(
		{
			@Index(fields={"locationId"},name="fpd_idxLocationId",unique=false),
			@Index(fields={"vendor"},name="fpd_idxVendor",unique=false),
			@Index(fields={"model"},name="fpd_idxModel",unique=false),
			@Index(fields={"serial"},name="fpd_idxSerial",unique=false),
			@Index(fields={"name"},name="fpd_name", unique=false)
		}
	)
@FmsAccessControlled(FmsAccessControlled.GROUP.PLAYERS)
public class FmsPlaybackDevice extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	private String locationName;

	private String locationId;
	private String vendor;
	private String model;
	private String serial;
	private String address;
	private int port;
	private String protocol;
	private String username;
	private String password;
	private String certificateId;
	private String deviceType;
	private FmsCertificateReference certificate;
	private boolean generateTrustedDeviceList;
	private boolean online;
	private boolean primary;
	private long total;
	private long used;
	
	private boolean disabled = false;

	private List<String> mediaClipTypes = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();

	private Map<String,Object> data = new HashMap<String, Object>();
	
	public FmsPlaybackDevice() {
		super(null);
	}

	public FmsPlaybackDevice(String id) {
		super(id);
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	@Searchable
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	@Searchable
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	@Searchable
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getDisplayName() {
		StringBuffer sb = new StringBuffer();
		if(getVendor()!=null) {
			sb.append(getVendor());
			sb.append(' ');
		}
		if(getModel()!=null) {
			sb.append(getModel());
			sb.append(' ');
		}
		if(getSerial()!=null) {
			sb.append(getSerial());
			sb.append(' ');
		}
		if(getLocationName()!=null) {
			sb.append(' ');
			sb.append('-');
			sb.append(' ');
			sb.append(getLocationName());
			sb.append(' ');
		}
		return sb.toString().trim();
	}

	public void setDisplayName(String displayName) {
	}

	public List<String> getMediaClipTypes() {
		return mediaClipTypes;
	}

	public void setMediaClipTypes(List<String> mediaClipTypes) {
		this.mediaClipTypes = mediaClipTypes;
	}

	public Map<String,Object> getData() {
		return data;
	}

	public void setData(Map<String,Object> data) {
		this.data = data;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isGenerateTrustedDeviceList() {
		return generateTrustedDeviceList;
	}

	public void setGenerateTrustedDeviceList(boolean generateTrustedDeviceList) {
		this.generateTrustedDeviceList = generateTrustedDeviceList;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public FmsCertificateReference getCertificate() {
		return certificate;
	}

	public void setCertificate(FmsCertificateReference certificate) {
		this.certificate = certificate;
	}
	
	@Override
	public String getSearchable() {
		return super.getSearchable()+" "+getDisplayName();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

}
