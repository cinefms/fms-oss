package com.openfms.utils.dcp.generic;

import java.util.Date;
import java.util.List;

import com.openfms.utils.dcp.smpte.pkl.UserText;

public class AssetMap {

	private Type type;
	
    protected String id;
    protected UserText annotationText;
    protected UserText creator;
    protected int volumeCount;
    protected Date issueDate;
    protected String issuer;
    protected List<AssetMapAsset> assets;
    
    public AssetMap() {
	}
    
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserText getAnnotationText() {
		return annotationText;
	}

	public void setAnnotationText(UserText annotationText) {
		this.annotationText = annotationText;
	}

	public UserText getCreator() {
		return creator;
	}

	public void setCreator(UserText creator) {
		this.creator = creator;
	}

	public int getVolumeCount() {
		return volumeCount;
	}

	public void setVolumeCount(int volumeCount) {
		this.volumeCount = volumeCount;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public List<AssetMapAsset> getAssets() {
		return assets;
	}

	public void setAssets(List<AssetMapAsset> assets) {
		this.assets = assets;
	}
    
}
