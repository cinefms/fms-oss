package com.openfms.model.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FmsAccessControlled {

	public static enum GROUP {
		HISTORY,
		COMMENTS,
		MOVIE,
		MEDIA,
		SCHEDULE, 
		TASKS, 
		SITES, 
		LOCATIONS, 
		PLAYERS, 
		PROJECT,
		TYPES, 
		TAGS,
		KEYS, 
		CERTIFICATES, 
		KEY_DATA, 
		CERTIFICATE_DATA, 
		KEY_REQUESTS, 
		USERS, 
		GROUPS,
		SCHEDULED_JOBS,
		SCHEDULED_JOB_LOGS,
		
	};
	
	
	public GROUP value();
	
}
