package com.openfms.model.core.playback;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.TYPES)
public class FmsPlaybackDeviceType extends AbstractFmsObject {
	
	private static final long serialVersionUID = 3147104749586074013L;

	public FmsPlaybackDeviceType() {
		super(null);
	}
	
	public String getId() {
		return getName();
	}
	

}
