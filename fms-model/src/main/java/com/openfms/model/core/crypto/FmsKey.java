package com.openfms.model.core.crypto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;
import com.openfms.model.core.references.FmsDeviceReference;
import com.openfms.model.core.references.FmsEventReference;


@Indexes(
		{
			@Index(fields={"movieId"},name="k_movieId",unique=false),
			@Index(fields={"movieVersionId"},name="k_movieVersionId",unique=false),
			@Index(fields={"certificateId"},name="k_certificateId",unique=false),
			@Index(fields={"externalId"},name="idxExternalId",unique=false),
			@Index(fields={"certificateDnQualifier"},name="idxCertificateDnQualifier",unique=false),
			@Index(fields={"validFrom"},name="idxValidFrom",unique=false),
			@Index(fields={"mediaClipExternalId"},name="idxmediaClipExternalId",unique=false),
			@Index(fields={"validTo"},name="idxValidTo",unique=false)
		}
	)
@FmsAccessControlled(FmsAccessControlled.GROUP.KEYS)
public class FmsKey extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	private String source;
	private String titleText;
	private String type;
	private String md5;
	
	private int status = 3;
	private int size = -1;

	private String externalId;

	private String movieId;

	private List<String> movieIds = new ArrayList<String>();
	private List<String> mediaClipIds = new ArrayList<String>();
	
	private String mediaClipExternalId;

	private String certificateDn;
	private String certificateDnQualifier;
	private String certificateCategory;

	private List<String> tdlThumbprints = new ArrayList<String>();
	
	private String certificateId;
	private List<FmsDeviceReference> devices = new ArrayList<FmsDeviceReference>();
	
	private String issuerDn;
	private String issuerDnQualifier;

	private Date validFrom;
	private Date validTo;
	
	private List<String> validCategories;
	private List<FmsEventReference> events = new ArrayList<FmsEventReference>();
	

	public FmsKey() {
		super(null);
	}

	public FmsKey(String id) {
		super(id);
	}

	public void setName(String name) {
		if(name != null) {
			String[] x = name.split("[\\\\/]");
			super.setName(x[x.length-1]);
		} else {
			super.setName(null);
		}
		
	}
	
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getMediaClipExternalId() {
		return mediaClipExternalId;
	}

	public void setmediaClipExternalId(String mediaClipExternalId) {
		this.mediaClipExternalId = mediaClipExternalId;
	}

	public String getCertificateDn() {
		return certificateDn;
	}

	public void setCertificateDn(String certificateDn) {
		this.certificateDn = certificateDn;
	}

	public String getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
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

	public String getCertificateDnQualifier() {
		return certificateDnQualifier;
	}

	public void setCertificateDnQualifier(String certificateDnQualifier) {
		this.certificateDnQualifier = certificateDnQualifier;
	}

	public String getIssuerDn() {
		return issuerDn;
	}

	public void setIssuerDn(String issuerDn) {
		this.issuerDn = issuerDn;
	}

	public String getIssuerDnQualifier() {
		return issuerDnQualifier;
	}

	public void setIssuerDnQualifier(String issuerDnQualifier) {
		this.issuerDnQualifier = issuerDnQualifier;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCertificateCategory() {
		return certificateCategory;
	}

	public void setCertificateCategory(String certificateCategory) {
		this.certificateCategory = certificateCategory;
	}

	public List<String> getValidCategories() {
		return validCategories;
	}

	public void setValidCategories(List<String> validCategories) {
		this.validCategories = validCategories;
	}

	public List<FmsEventReference> getEvents() {
		return events;
	}

	public void setEvents(List<FmsEventReference> events) {
		this.events = events;
	}

	public List<String> getMovieIds() {
		return movieIds;
	}

	public void setMovieIds(List<String> movieIds) {
		if(movieIds==null) {
			this.movieIds.clear();
			return;
		}
		this.movieIds = movieIds;
	}

	public List<String> getMediaClipIds() {
		return mediaClipIds;
	}

	public void setMediaClipIds(List<String> mediaClipIds) {
		if(mediaClipIds==null) {
			this.mediaClipIds.clear();
			return;
		}
		this.mediaClipIds = mediaClipIds;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<FmsDeviceReference> getDevices() {
		return devices;
	}

	public void setDevices(List<FmsDeviceReference> devices) {
		this.devices = devices;
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

}
