package com.openfms.model.core.auth;

import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.openfms.model.core.AbstractFmsObject;

public class FmsPassword extends AbstractFmsObject {

	private static final long serialVersionUID = 8116458948032965249L;
	private String userId;
	private String cryptedPassword;
	private String newCryptedPassword;
	private String resetToken;
	private Date resetTokenExpires;

	public String getCryptedPassword() {
		return cryptedPassword;
	}

	public void setCryptedPassword(String cryptedPassword) {
		this.cryptedPassword = cryptedPassword;
	}

	public void setPassword(String password) {
		this.cryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public boolean compare(String password) {
		return BCrypt.checkpw(password, cryptedPassword);
	}

	public boolean compareNew(String password) {
		if(newCryptedPassword==null) {
			return false;
		}
		return BCrypt.checkpw(password, newCryptedPassword);
	}
	
	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public Date getResetTokenExpires() {
		return resetTokenExpires;
	}

	public void setResetTokenExpires(Date resetTokenExpires) {
		this.resetTokenExpires = resetTokenExpires;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setNewPassword(String newPassword) {
		this.cryptedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
	}

	public String getNewCryptedPassword() {
		return newCryptedPassword;
	}

	public void setNewCryptedPassword(String newCryptedPassword) {
		this.newCryptedPassword = newCryptedPassword;
	}

	

}
