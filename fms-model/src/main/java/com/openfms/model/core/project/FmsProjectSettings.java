package com.openfms.model.core.project;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.PROJECT)
public class FmsProjectSettings extends AbstractFmsObject {
	
	private static final long serialVersionUID = 4953078314156612594L;

	private String timezone;
	private String dateFormat;
	private String timeFormat;
	
	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

}
