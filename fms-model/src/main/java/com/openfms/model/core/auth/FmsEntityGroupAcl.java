package com.openfms.model.core.auth;

import java.util.ArrayList;
import java.util.List;

import com.openfms.model.annotations.FmsAccessControlled;

public class FmsEntityGroupAcl {

	private FmsAccessControlled.GROUP group;
	private List<AccessType> access = new ArrayList<AccessType>();

	public FmsEntityGroupAcl() {
	}
	
	public FmsAccessControlled.GROUP getGroup() {
		return group;
	}

	public void setGroup(FmsAccessControlled.GROUP group) {
		this.group = group;
	}

	
	public void setAccess(List<AccessType> access) {
		if(access == null) {
			this.access.clear();
			return;
		}
		List<AccessType> n = new ArrayList<AccessType>(access);
		this.access = n;
	}
	
	public List<AccessType> getAccess() {
		return access;
	}
	
}
