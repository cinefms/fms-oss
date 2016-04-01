package com.openfms.model.core.movie;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.TYPES)
public class FmsMoviePackageType extends AbstractFmsObject {
	
	private static final long serialVersionUID = -401134205329943664L;

	public FmsMoviePackageType() {
		super(null);
	}

	@Override
	public String getId() {
		return super.getName();
	}
	

}
