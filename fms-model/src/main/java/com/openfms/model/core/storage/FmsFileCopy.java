package com.openfms.model.core.storage;

import java.util.Date;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;


@FmsAccessControlled(FmsAccessControlled.GROUP.MEDIA)
public class FmsFileCopy extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	private String packageCopyId;

	private String fileId;
	private String packageId;

	private String quickhash;
	private String md5;
	private String relativePath;
	private Date lastUpdate;
	private long size;
	private long complete;
	private boolean ok;
	
	
	public FmsFileCopy() {
		super(null);
	}

	public FmsFileCopy(String id) {
		super(id);
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getQuickhash() {
		return quickhash;
	}

	public void setQuickhash(String quickhash) {
		this.quickhash = quickhash;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getComplete() {
		return complete;
	}

	public void setComplete(long complete) {
		this.complete = complete;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getPackageCopyId() {
		return packageCopyId;
	}

	public void setPackageCopyId(String packageCopyId) {
		this.packageCopyId = packageCopyId;
	}


}
