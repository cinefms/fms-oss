package com.openfms.model.core.playback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openfms.model.Status;
import com.openfms.model.core.AbstractFmsObject;

public class FmsCinemaServerStatus extends AbstractFmsObject {

	private static final long serialVersionUID = -5957779870343220740L;
	
	private int status = Status.NOT_APPLICABLE.value();
	private Date date;
	private String deviceId;
	private String eventId;
	
	private List<FmsCinemaServerCplStatus> cplStatus = new ArrayList<FmsCinemaServerCplStatus>();
	private FmsCinemaServerDiskStatus diskStatus;

	public List<FmsCinemaServerCplStatus> getCplStatus() {
		return cplStatus;
	}

	public void setCplStatus(List<FmsCinemaServerCplStatus> cplStatus) {
		this.cplStatus = cplStatus;
	}

	public FmsCinemaServerDiskStatus getDiskStatus() {
		return diskStatus;
	}

	public void setDiskStatus(FmsCinemaServerDiskStatus diskStatus) {
		this.diskStatus = diskStatus;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@JsonIgnore
	public FmsCinemaServerStatusBrief getBrief() {
		FmsCinemaServerStatusBrief out = new FmsCinemaServerStatusBrief();
		out.setCpls(cplStatus.size());
		out.setDate(getDate());
		out.setId(getId());
		
		if(getDiskStatus()!=null) {
			out.setTotal(getDiskStatus().getTotalSpace());
			out.setUsed(getDiskStatus().getTotalSpace()-getDiskStatus().getAvailableSpace());
		}
		return out;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	
}
