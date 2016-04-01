package com.openfms.model.core.playback;

import com.openfms.model.core.AbstractFmsObject;

public class FmsDeviceProtocol extends AbstractFmsObject {

	private static final long serialVersionUID = 6373322363027412959L;

	public FmsDeviceProtocol() {
		super(null);
	}

	public FmsDeviceProtocol(String id) {
		super(id);
	}

	@Override
	public String getName() {
		return super.getId();
	}
	
	
}
