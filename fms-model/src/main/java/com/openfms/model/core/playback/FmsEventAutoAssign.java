package com.openfms.model.core.playback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.SCHEDULE)
public class FmsEventAutoAssign extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	private boolean checkAudioLanguage = true;
	private boolean checkSubtitleLanguage = true;

	private boolean success = false;
	private String message;
	
	private Date startTime;
	private Date endTime;
	private List<FmsEventAutoAssignLog> log = new ArrayList<>();
	
	public FmsEventAutoAssign() {
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getModified() {
		int out = 0;
		for (FmsEventAutoAssignLog li : log) {
			out = out + li.getModified();
		}
		return out;
	}

	public void setModified(int modified) {
	}

	public int getTotal() {
		int out = 0;
		for (FmsEventAutoAssignLog li : log) {
			out = out + li.getTotal();
		}
		return out;
	}

	public void setTotal(int total) {
	}

	public List<FmsEventAutoAssignLog> getLog() {
		return log;
	}

	public void setLog(List<FmsEventAutoAssignLog> log) {
		this.log = log;
	}

	public boolean isCheckAudioLanguage() {
		return checkAudioLanguage;
	}

	public void setCheckAudioLanguage(boolean checkAudioLanguage) {
		this.checkAudioLanguage = checkAudioLanguage;
	}

	public boolean isCheckSubtitleLanguage() {
		return checkSubtitleLanguage;
	}

	public void setCheckSubtitleLanguage(boolean checkSubtitleLanguage) {
		this.checkSubtitleLanguage = checkSubtitleLanguage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
