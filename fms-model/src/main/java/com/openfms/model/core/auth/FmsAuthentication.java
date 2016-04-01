package com.openfms.model.core.auth;

public class FmsAuthentication {

	private String type;
	private String emailAddress;
	private String password;
	private String username;
	private String newPassword;
	private String newPasswordToken;

	public FmsAuthentication() {
	}
	
	public FmsAuthentication(String emailAddress, String username, String password) {
		super();
		this.emailAddress = emailAddress;
		this.password = password;
		this.username = username;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordToken() {
		return newPasswordToken;
	}

	public void setNewPasswordToken(String newPasswordToken) {
		this.newPasswordToken = newPasswordToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
