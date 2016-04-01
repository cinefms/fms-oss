package com.openfms.model.core.movie;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.TYPES)
public class FmsScreenAspect extends AbstractFmsObject {

	public FmsScreenAspect() {
		super(null);
	}
	
	@Override
	public String getId() {
		return super.getName();
	}

}
