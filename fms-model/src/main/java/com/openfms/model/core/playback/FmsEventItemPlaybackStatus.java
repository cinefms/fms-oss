package com.openfms.model.core.playback;

import java.util.ArrayList;
import java.util.List;

import com.openfms.model.Status;
import com.openfms.model.utils.StatusCombine;

public class FmsEventItemPlaybackStatus {

	private String errorMessage;
	private int status = Status.NOT_APPLICABLE.value();
	private boolean playable; 
	private boolean complete; 
	
	private List<MediaClipPlaybackStatus> mediaClipStatus = new ArrayList<MediaClipPlaybackStatus>(); 

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isPlayable() {
		return playable;
	}

	public void setPlayable(boolean playable) {
		this.playable = playable;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void add(MediaClipPlaybackStatus s) {
		if(mediaClipStatus == null) {
			mediaClipStatus = new ArrayList<MediaClipPlaybackStatus>();
		}
		mediaClipStatus.add(s);
		this.status = StatusCombine.combine(this.status,s.getStatus());
	}

}
