package com.openfms.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.openfms.core.service.SessionStore;
import com.openfms.model.core.auth.FmsSession;

@EnableScheduling
public class InMemorySessionStore implements SessionStore {

	private static Log log = LogFactory.getLog(InMemorySessionStore.class);
	
	private Map<String,SessionInfo> sessions = new HashMap<String, InMemorySessionStore.SessionInfo>();
	
	@Override
	public FmsSession getSession(String key) {
		SessionInfo si = sessions.get(key);
		if(si != null && si.isValid()) {
			si.reset();
			return si.getSession();
		}
		return null;
	}

	@Override
	public void updateSession(FmsSession session) {
		SessionInfo si = new SessionInfo();
		si.setSession(session);
		si.reset();
		sessions.put(session.getKey(), si);
	}

	@Override
	public void destroySession(FmsSession session) {
		sessions.remove(session.getKey());
	}

	@Override
	@Scheduled(cron="0 0/5 * * * *")
	public void cleanup() {
		int a = sessions.size();
		try {
			for(Map.Entry<String, SessionInfo> infos : new HashMap<String,SessionInfo>(sessions).entrySet()) {
				if(System.currentTimeMillis() - infos.getValue().getDate().getTime() > 600000) {
					log.info(" --- SESSION CLEANUP: cleaning up stale session: "+infos.getKey()+" "+" / "+infos.getValue().getDate());
					sessions.remove(infos.getKey());
				}
			}
		} catch (Exception e) {
			log.error("error in session cleanup: ",e);
		}
		int b = sessions.size();
		log.info(" --- SESSION CLEANUP FINISHED: "+a+" --> "+b);
	}
	
	
	private class SessionInfo {

		private FmsSession session;
		private Date date = new Date();

		public FmsSession getSession() {
			return session;
		}

		public void setSession(FmsSession session) {
			this.session = session;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
		
		public void reset() {
			this.date = new Date();
		}

		public boolean isValid() {
			return System.currentTimeMillis() < date.getTime()+(30*60*1000); 
		}

	}

}
