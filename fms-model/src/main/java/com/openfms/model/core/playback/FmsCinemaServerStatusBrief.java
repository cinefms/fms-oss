package com.openfms.model.core.playback;

import java.util.Date;

public class FmsCinemaServerStatusBrief {

	private static final long serialVersionUID = -5957779870343220740L;

	private String id;
	private Date date;
	private long total;
	private long used;
	private int cpls;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public int getCpls() {
		return cpls;
	}

	public void setCpls(int cpls) {
		this.cpls = cpls;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
