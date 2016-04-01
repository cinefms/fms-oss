package com.openfms.model.core.global;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.TAGS)
public class FmsTag extends AbstractFmsObject {

	private static final long serialVersionUID = 2037193066225570037L;
	
	@Override
	public String getId() {
		return super.getName();
	}
	
}
