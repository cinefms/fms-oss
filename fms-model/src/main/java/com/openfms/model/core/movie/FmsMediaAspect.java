package com.openfms.model.core.movie;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.TYPES)
public class FmsMediaAspect extends AbstractFmsObject {

	private static final long serialVersionUID = -7177733268898542803L;

	public FmsMediaAspect() {
		super(null);
	}
	
	@Override
	public String getId() {
		return getName();
	}
	
	

}
