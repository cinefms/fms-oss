package com.openfms.model.core.playback;

import java.util.ArrayList;
import java.util.List;

public class FmsCinemaServerCplStatus implements Comparable<FmsCinemaServerCplStatus>{
	
	private String cplUuid, contentTitleText, lastErrorMessage;
	private long size;
	private boolean complete;
	private boolean playable;
	private boolean encrypted;
	private List<String> keyIds = new ArrayList<String>();
	private String mediaClipId;

	public String getCplUuid() {
		return cplUuid;
	}

	public void setCplUuid(String cplUuid) {
		this.cplUuid = cplUuid;
	}

	public List<String> getKeyIds() {
		return keyIds;
	}

	public void setKeyIds(List<String> keyIds) {
		this.keyIds = keyIds;
	}

	public String getContentTitleText() {
		return contentTitleText!=null?contentTitleText.replace('\u0000', ' ').trim():"[NULL]";
	}

	public void setContentTitleText(String contentTitleText) {
		this.contentTitleText = contentTitleText;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getLastErrorMessage() {
		return lastErrorMessage;
	}

	public void setLastErrorMessage(String lastErrorMessage) {
		this.lastErrorMessage = lastErrorMessage;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public boolean isPlayable() {
		return playable;
	}

	public void setPlayable(boolean playable) {
		this.playable = playable;
	}

	public int compareTo(FmsCinemaServerCplStatus arg0) {
		return getContentTitleText().compareTo(arg0.getContentTitleText());
	}

	public String getMediaClipId() {
		return mediaClipId;
	}

	public void setMediaClipId(String mediaClipId) {
		this.mediaClipId = mediaClipId;
	}

}
