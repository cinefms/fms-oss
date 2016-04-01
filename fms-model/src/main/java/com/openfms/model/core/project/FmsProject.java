package com.openfms.model.core.project;

import java.util.ArrayList;
import java.util.List;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.core.AbstractFmsObject;

@Indexes(
	{
		@Index(name="unique_project_name",fields={"name"},unique=true),
		@Index(name="unique_project_shortname",fields={"shortName"},unique=true)
	}
)
public class FmsProject extends AbstractFmsObject {

	private static final long serialVersionUID = 462979936836938141L;
	
	private List<String> hostnames = new ArrayList<String>(); 
	
	private String name;
	private String shortName;
	private String owner;
	private String timezone;
	private String timeFormat;
	private String dateFormat;
	
	private boolean active = false;

	public FmsProject() {
		super(null);
	}
	
	public FmsProject(String id) {
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getHostnames() {
		return hostnames;
	}

	public void setHostnames(List<String> hostnames) {
		this.hostnames = hostnames;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTimezone() {
		return timezone==null?"Europe/Berlin":timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getTimeFormat() {
		return timeFormat==null?"HH:mm":timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getDateFormat() {
		return dateFormat==null?"YYYY-MM-DD":dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
}
