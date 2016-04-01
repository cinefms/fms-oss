package com.openfms.model.core.auth;

import java.util.ArrayList;
import java.util.List;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@Indexes(
			{
				@Index(name="unique_group_name_per_project",fields={"name","projectId"},unique=true),
				@Index(fields={"name"},name="fg_name", unique=false)
			}
		)
@FmsAccessControlled(FmsAccessControlled.GROUP.GROUPS)
public class FmsGroup extends AbstractFmsObject {

	private static final long serialVersionUID = -5191498777450850961L;
	private List<FmsEntityGroupAcl> acls = new ArrayList<FmsEntityGroupAcl>();
	
	
	public FmsGroup() {
		super(null);
	}

	public FmsGroup(String id) {
		super(id);
	}

	public List<FmsEntityGroupAcl> getAcls() {
		return acls;
	}

	public void setAcls(List<FmsEntityGroupAcl> acls) {
		if(acls == null) {
			this.acls.clear();
			return;
		}
		List<FmsEntityGroupAcl> n = new ArrayList<FmsEntityGroupAcl>(acls);
		this.acls = n;
	}

}
