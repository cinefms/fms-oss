package com.openfms.model.core.auth;

import java.util.Date;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@Indexes(
		{
			@Index(name="modifiedObjectId",fields={"h_mod_object_id"},unique=false),
			@Index(name="modifiedObjectType",fields={"h_mod_object_type"},unique=false),
		}
)
@FmsAccessControlled(FmsAccessControlled.GROUP.HISTORY)
public class FmsHistory extends AbstractFmsObject {
	
	private String username;
	private String userId;
	private String modifiedObjectId;
	private String modifiedObjectType;
	private Date date;
	private String operation;
	private Object object;
	
	private String searchable;
	
	private static final long serialVersionUID = 4427365661228274826L;

	public FmsHistory() {
		super(null);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getModifiedObjectId() {
		return modifiedObjectId;
	}

	public void setModifiedObjectId(String modifiedObjectId) {
		this.modifiedObjectId = modifiedObjectId;
	}

	public String getModifiedObjectType() {
		return modifiedObjectType;
	}

	public void setModifiedObjectType(String modifiedObjectType) {
		this.modifiedObjectType = modifiedObjectType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getSearchable() {
		return searchable;
	}

	public void setSearchable(String searchable) {
		this.searchable = searchable;
	}

}
