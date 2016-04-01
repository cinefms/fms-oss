package com.openfms.model.core.auth;

import java.util.HashMap;
import java.util.Map;

import com.openfms.model.core.AbstractFmsObject;

public class FmsUserSettings extends AbstractFmsObject {

	private static final long serialVersionUID = 8660024706468037110L;

	private String userId;
	private Map<String, String> properties = new HashMap<String, String>();

	public FmsUserSettings() {
		super(null);
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

}
