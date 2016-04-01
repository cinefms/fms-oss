package com.openfms.core.service.project.impl.listeners;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsLocationService;
import com.openfms.core.service.project.FmsScheduledJobConfigService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.auth.FmsProjectUser;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobConfig;
import com.openfms.model.core.scheduledjobs.FmsScheduledJobConfigUser;

public class ScheduledJobConfigListener extends FmsListenerAdapter<FmsScheduledJobConfig> {
	
	
	@Autowired
	private FmsLocationService locationService;
	
	@Autowired
	private FmsScheduledJobConfigService playbackDeviceService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	
	protected ScheduledJobConfigListener() {
	}

	
	@Override
	protected boolean beforeSave(String db, FmsScheduledJobConfig object) {
		try {
			List<FmsScheduledJobConfigUser> users = new ArrayList<FmsScheduledJobConfigUser>();
			if(object.getUserIds()!=null) {
				for(String id : object.getUserIds()) {
					FmsUser user = dataStore.getObject(FmsProjectUser.class, id);
					users.add(new FmsScheduledJobConfigUser(user));
				}
			}
			object.setUsers(users);
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data ... ", e);
		}
		return true;
		
	}

}
