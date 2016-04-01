package com.openfms.model.core.playback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openfms.model.Status;
import com.openfms.model.core.AbstractFmsObject;

@Indexes(
		{
			@Index(fields={"eventId"},name="idxEId",unique=false),
			@Index(fields={"date"},name="idxDt",unique=false),
			@Index(fields={"status"},name="idxSts",unique=false),
		}
	)
public class FmsEventPBS extends AbstractFmsObject {
	
	private static final long serialVersionUID = 4198406480767513741L;
	private Date date;
	private String eventId;
	private String deviceId;
	private List<MediaClipPlaybackStatus> mediaClipStatus = new ArrayList<MediaClipPlaybackStatus>();

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public List<MediaClipPlaybackStatus> getMediaClipStatus() {
		return mediaClipStatus;
	}

	public void setMediaClipStatus(List<MediaClipPlaybackStatus> mediaClipStatus) {
		this.mediaClipStatus = mediaClipStatus;
	}

	public void add(MediaClipPlaybackStatus s) {
		if(this.mediaClipStatus==null) {
			this.mediaClipStatus = new ArrayList<MediaClipPlaybackStatus>();
		}
		this.mediaClipStatus.add(s);
	}
	
	@JsonIgnore
	public FmsEventPlaybackStatusBrief getBrief() {
		FmsEventPlaybackStatusBrief out = new FmsEventPlaybackStatusBrief(this);
		return out;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
