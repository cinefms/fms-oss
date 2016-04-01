package com.openfms.model.core.movie;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.TYPES)
public class FmsFramerate extends AbstractFmsObject {
	
	private static final long serialVersionUID = 988099359421711862L;

	public FmsFramerate() {
	}
	
	@Override
	public String getId() {
		return getName();
	}
	

}
