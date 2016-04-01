package com.openfms.model.core.playback;

import java.util.Date;

public class FmsEventPlaybackStatusBrief {

	private String id;
	private Date date;
	
	public FmsEventPlaybackStatusBrief(FmsEventPBS fmsEventPlaybackStatus) {
		this.id = fmsEventPlaybackStatus.getId();
		this.date = fmsEventPlaybackStatus.getCreated();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


}
