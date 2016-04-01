package com.openfms.model.core.media;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFmsMediaInfo {

	private int bitrate;
	private boolean encrypted;
	private String format;
	private Map<String, Object> details = new HashMap<String, Object>();

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

}
