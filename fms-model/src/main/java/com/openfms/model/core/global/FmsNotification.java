package com.openfms.model.core.global;

import java.util.Date;

import com.openfms.model.core.AbstractFmsObject;

public class FmsNotification extends AbstractFmsObject {

	private static final long serialVersionUID = -357447659403635174L;

	private String from;
	private String to;
	private boolean read = false;
	private String commentId;
	private String rootObjectId;
	private String rootObjectType;
	private String message;

	private Date date = new Date();
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getRootObjectId() {
		return rootObjectId;
	}

	public void setRootObjectId(String rootObjectId) {
		this.rootObjectId = rootObjectId;
	}

	public String getRootObjectType() {
		return rootObjectType;
	}

	public void setRootObjectType(String rootObjectType) {
		this.rootObjectType = rootObjectType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
