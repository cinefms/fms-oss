package com.openfms.model.core.auth;

import java.util.ArrayList;
import java.util.List;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;

@Indexes(
		{
			@Index(name="pu_unique_username",fields={"username"},unique=true)
		}
)
@FmsAccessControlled(FmsAccessControlled.GROUP.USERS)
public class FmsProjectUser extends FmsUser {

	private static final long serialVersionUID = 9014426533506320943L;

	private List<String> groups = new ArrayList<String>();
	private List<String> movieIds = new ArrayList<String>();

	public FmsProjectUser() {
		super();
	}

	@Override
	public boolean isAdmin() {
		return false;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		if (groups == null) {
			this.groups.clear();
			return;
		}
		this.groups = groups;
	}

	public List<String> getMovieIds() {
		return movieIds;
	}

	public void setMovieIds(List<String> movieIds) {
		if (movieIds == null) {
			this.movieIds.clear();
			return;
		}
		this.movieIds = movieIds;
	}

	
	@Override
	public String getSearchable() {
		return super.getSearchable()+"  "+getUsername();
	}
	
}
