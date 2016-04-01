package com.openfms.model.core.playback;

import java.util.HashMap;
import java.util.Map;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;


@FmsAccessControlled(FmsAccessControlled.GROUP.SITES)
public class FmsSite extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;
	
	private Map<String,Object> data = new HashMap<String, Object>();
	private String externalId;

	public FmsSite() {
		super(null);
	}

	public FmsSite(String id) {
		super(id);
	}

	public Map<String,Object> getData() {
		return data;
	}

	public void setData(Map<String,Object> data) {
		this.data = data;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}


}
