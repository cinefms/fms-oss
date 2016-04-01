package com.openfms.model.core.movie;

import java.util.ArrayList;
import java.util.List;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.TYPES)
public class FmsMediaClipTaskType extends AbstractFmsObject {
	
	private static final long serialVersionUID = 3627178911341519345L;

	private List<String> generateForMediaClipTypes = new ArrayList<String>();
	private List<String> enableForMediaClipTypes = new ArrayList<String>();
	private List<FmsMediaClipTaskTypeNext> next = new ArrayList<FmsMediaClipTaskTypeNext>();
	private String deviceId;
	private String deviceName;

	public FmsMediaClipTaskType() {
		super(null);
	}

	@Override
	public String getId() {
		return getName();
	}
	
	public List<String> getGenerateForMediaClipTypes() {
		return generateForMediaClipTypes;
	}

	public void setGenerateForMediaClipTypes(List<String> generateForMediaClipTypes) {
		this.generateForMediaClipTypes = generateForMediaClipTypes;
	}

	public List<FmsMediaClipTaskTypeNext> getNext() {
		if(next == null) {
			next = new ArrayList<FmsMediaClipTaskTypeNext>();
		}
		return next;
	}

	public void setNext(List<FmsMediaClipTaskTypeNext> next) {
		this.next.clear();
		if(next != null) {
			this.next.addAll(next);
		}
	}

	public List<String> getEnableForMediaClipTypes() {
		return enableForMediaClipTypes;
	}

	public void setEnableForMediaClipTypes(List<String> enableForMediaClipTypes) {
		this.enableForMediaClipTypes = enableForMediaClipTypes;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	

}
