package com.openfms.model.core;

import java.io.Serializable;
import java.util.Date;

import com.cinefms.dbstore.api.DBStoreEntity;

public interface FmsObject extends DBStoreEntity, Serializable {

	public abstract void setId(String id);

	public abstract String getId();

	public abstract void setVersion(long version);

	public abstract long getVersion();

	public abstract void setCreated(Date created);

	public abstract Date getCreated();

	public abstract void setUpdated(Date updated);

	public abstract Date getUpdated();

	public abstract void setSearchable(String searchable);

	public abstract void setName(String name);

	public abstract String getName();
	
}
