package com.openfms.model.core.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.openfms.model.core.project.FmsProjectHolder;

public class FmsSessionHolder {

	private static Log log = LogFactory.getLog(FmsSessionHolder.class);
	
	private static ThreadLocal<FmsSession> holder = new ThreadLocal<FmsSession>();

	public static FmsSession get() {
		return holder.get();
	}

	public static void set(FmsSession session) {
		if (holder.get() != null) {
			if(log.isDebugEnabled()){ log.debug("replacing " + holder.get() + " with " + session); }
		}
		holder.set(session);
	}

	public static void clear() {
		holder.remove();
	}

	public static FmsUser getCurrentUser() {
		FmsSession session = get();
		if (session != null) {
			return session.getUser();
		}
		return null;
	}

	public static String getSessionCookieName() {
		String project = "FmsSessionId_admin";
		if (FmsProjectHolder.get() != null) {
			project = "FmsSessionId_project_"+FmsProjectHolder.get().getShortName();
		}
		return project;
	}

	
}
