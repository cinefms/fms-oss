package com.openfms.model.core.storage;

import java.util.Date;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;


@FmsAccessControlled(FmsAccessControlled.GROUP.MEDIA)
public class FmsStorageSystem extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	private String locationId;
	private String externalId;
	private String type;
	private boolean permanent;
	private boolean online;
	private long total;
	private long used;
	private Date lastUpdate;

	public FmsStorageSystem() {
		super(null);
	}

	public FmsStorageSystem(String id) {
		super(id);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isPermanent() {
		return permanent;
	}

	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
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

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	
}
