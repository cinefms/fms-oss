package com.openfms.core.api.v2.utils;

import java.util.Date;

public class DateRange {

	private Date from;
	private Date to;
	
	public DateRange() {
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return from+" - "+to;
	}
	
}
