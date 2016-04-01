package com.openfms.model.core.scheduledjobs;

import com.openfms.model.annotations.FmsAccessControlled;

@FmsAccessControlled(FmsAccessControlled.GROUP.TYPES)
public class FmsScheduledJobType {

	private String clazz;
	private String id;
	private String name;
	private String defaultConfig;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultConfig() {
		return defaultConfig;
	}

	public void setDefaultConfig(String defaultConfig) {
		this.defaultConfig = defaultConfig;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
