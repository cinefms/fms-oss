package com.openfms.model.core.storage;

import java.util.Date;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;


@Indexes(
		{
			@Index(fields={"storageSystemId"},name="mpc_storageSystemId",unique=false),
			@Index(fields={"moviePackageId"},name="mpc_moviePackageId",unique=false),
			@Index(fields={"lastUpdate"},name="mpc_lastUpdate",unique=false),
			@Index(fields={"size"},name="mpc_size",unique=false)		}
	)
@FmsAccessControlled(FmsAccessControlled.GROUP.MEDIA)
public class FmsMoviePackageCopy extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	private String storageSystemId;
	private String moviePackageId;
	private String relativePath;
	private Date lastUpdate;
	private long size;
	private long complete;
	private boolean ok;
	
	public FmsMoviePackageCopy() {
		super(null);
	}

	public FmsMoviePackageCopy(String id) {
		super(id);
	}

	public String getStorageSystemId() {
		return storageSystemId;
	}

	public void setStorageSystemId(String storageSystemId) {
		this.storageSystemId = storageSystemId;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
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

	public String getMoviePackageId() {
		return moviePackageId;
	}

	public void setMoviePackageId(String moviePackageId) {
		this.moviePackageId = moviePackageId;
	}


}
