package com.openfms.model.core.config;

import com.openfms.model.core.AbstractFmsObject;

public class FmsCredentials extends AbstractFmsObject {

	private static final long serialVersionUID = 5937834564771617858L;

	private String username;
	private String password;

	private String name;
	
	public FmsCredentials() {
		super(null);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
