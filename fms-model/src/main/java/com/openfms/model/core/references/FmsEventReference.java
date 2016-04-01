package com.openfms.model.core.references;

import java.util.Date;

import com.openfms.model.Status;
import com.openfms.model.core.playback.FmsEvent;

public class FmsEventReference {

	private String eventId;
	private String eventExternalId;
	private String eventName;
	private String locationId;
	private String locationName;
	private Date screeningDate;

	private int mediaStatus=Status.ERROR.value();
	private int versionStatus=Status.ERROR.value();
	private int encryptionStatus=Status.ERROR.value();
	private int playbackStatus=Status.ERROR.value();

	public FmsEventReference() {
		
	}
	
	public FmsEventReference(FmsEvent e) {
		this.eventId = e.getId();
		this.locationId = e.getLocationId();
		this.locationName = e.getLocationName();
		this.screeningDate = e.getStartTime();
		this.mediaStatus = e.getMediaStatus();
		this.versionStatus = e.getVersionStatus();
		this.encryptionStatus = e.getEncryptionStatus();
		this.playbackStatus = e.getPlaybackStatus();
		this.eventExternalId = e.getExternalId();
		this.eventName = e.getName();
		
	}

	public Date getScreeningDate() {
		return screeningDate;
	}

	public void setDate(Date screeningDate) {
		this.screeningDate = screeningDate;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getEventExternalId() {
		return eventExternalId;
	}

	public void setEventExternalId(String eventExternalId) {
		this.eventExternalId = eventExternalId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public int getMediaStatus() {
		return mediaStatus;
	}

	public void setMediaStatus(int mediaStatus) {
		this.mediaStatus = mediaStatus;
	}

	public int getVersionStatus() {
		return versionStatus;
	}

	public void setVersionStatus(int versionStatus) {
		this.versionStatus = versionStatus;
	}

	public int getEncryptionStatus() {
		return encryptionStatus;
	}

	public void setEncryptionStatus(int encryptionStatus) {
		this.encryptionStatus = encryptionStatus;
	}

	public int getPlaybackStatus() {
		return playbackStatus;
	}

	public void setPlaybackStatus(int playbackStatus) {
		this.playbackStatus = playbackStatus;
	}

}
