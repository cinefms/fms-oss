package com.openfms.model.core.playback;

public class FmsCinemaServerDiskStatus {

	private long totalSpace = -1;
	private long availableSpace = -1;

	public long getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(long totalSpace) {
		this.totalSpace = totalSpace;
	}

	public long getAvailableSpace() {
		return availableSpace;
	}
	
	public void setAvailableSpace(long availableSpace) {
		this.availableSpace = availableSpace;
	}

}
