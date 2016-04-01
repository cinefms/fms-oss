package com.openfms.model.core.playback;

public class MediaClipPlaybackStatus {

	private String mediaClipExternalId;
	private String message;
	private int status;

	public String getMediaClipExternalId() {
		return mediaClipExternalId;
	}

	public void setMediaClipExternalId(String mediaClipExternalId) {
		this.mediaClipExternalId = mediaClipExternalId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
