package com.openfms.model.core.auth;

import java.util.HashMap;
import java.util.Map;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.Searchable;
import com.openfms.model.core.AbstractFmsObject;

@Indexes(
		{
		@Index(name="unique_username",fields={"usernameUpper"},unique=true),
		@Index(name="unique_email_address",fields={"emailUpper"},unique=true),
		@Index(fields={"name"},name="fu_name", unique=false)
		}
)
public class FmsUser extends AbstractFmsObject {

	private static final long serialVersionUID = 8660024706468037110L;

	private String email;
	private String username;
	private boolean enabled;
	private Map<String,String> properties = new HashMap<String, String>();

	public FmsUser() {
		super(null);
	}
	
	public FmsUser(String id) {
		super(id);
	}
	
	@Searchable
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAdmin() {
		return false;
	}

	public void setAdmin(boolean admin) {
	}

	public String getEmailUpper() {
		if(email!=null) {
			return email.toUpperCase();
		}
		return null;
	}

	public void setEmailUpper(String emailUpper) {
	}

	public String getDisplayName() {
		StringBuffer sb = new StringBuffer();
		if(getName()!=null) {
			sb.append(getName());
		}
		sb.append(" ("+getName()+")");
		return sb.toString().trim();
	}

	public void setDisplayName(String displayName) {
	}

	public Map<String,String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String,String> properties) {
		this.properties = properties;
	}

	@Searchable
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsernameUpper() {
		if(username!=null) {
			return username.toUpperCase();
		}
		return null;
	}

	public void setUsernameUpper(String usernameUpper) {
	}

	

}
