package com.openfms.model.core.playback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;
import com.openfms.model.core.references.FmsCertificateReference;
import com.openfms.model.core.references.FmsDeviceReference;


@Indexes(
		{
			@Index(fields={"siteId"},name="fl_siteId",unique=false),
			@Index(fields={"externalId"},name="fl_externalId",unique=false),
			@Index(fields={"name"},name="fl_name", unique=false),
		}
	)
@FmsAccessControlled(FmsAccessControlled.GROUP.LOCATIONS)
public class FmsLocation extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	private String siteId;
	private String siteName;
	private String externalId;
	private String externalSiteId;
	private List<String> tags = new ArrayList<String>();
	
	private List<FmsDeviceReference> devices;
	private List<FmsCertificateReference> certificates;
	
	private boolean active = true;
	
	private Map<String,Object> data;
 	
	public FmsLocation() {
		super(null);
	}

	public FmsLocation(String id) {
		super(id);
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		if(tags==null) {
			this.tags.clear();
			return;
		}
		this.tags = tags;
	}

	public List<FmsDeviceReference> getDevices() {
		return devices;
	}

	public void setDevices(List<FmsDeviceReference> devices) {
		this.devices = devices;
	}

	public List<FmsCertificateReference> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<FmsCertificateReference> certificates) {
		this.certificates = certificates;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getExternalSiteId() {
		return externalSiteId;
	}

	public void setExternalSiteId(String externalSiteId) {
		this.externalSiteId = externalSiteId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Map<String,Object> getData() {
		return data;
	}

	public void setData(Map<String,Object> data) {
		this.data = data;
	}


}
