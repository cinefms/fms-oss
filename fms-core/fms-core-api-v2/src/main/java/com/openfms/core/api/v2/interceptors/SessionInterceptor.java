package com.openfms.core.api.v2.interceptors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.openfms.core.service.AuthnService;
import com.openfms.model.core.auth.FmsSession;
import com.openfms.model.core.auth.FmsSessionHolder;

public class SessionInterceptor extends HandlerInterceptorAdapter {
	
	private static Log log = LogFactory.getLog(SessionInterceptor.class);

	@Autowired
	private AuthnService authenticationService;
	
	private void addCookie(HttpServletResponse response, String key, long expires) {
		Cookie c = new Cookie(FmsSessionHolder.getSessionCookieName(), key);
		if(log.isDebugEnabled()){ log.debug(" INTERCEPTOR ADDING cookies: "+key+" / "+expires); }
		if(expires>-1) {
			c.setMaxAge(0);
		}
		c.setPath("/");
		response.addCookie(c);
	}
	
	
	private List<String> getSessionIds(HttpServletRequest request, HttpServletResponse response) {
		List<String> out = new ArrayList<String>();
		String sessionCookieName = FmsSessionHolder.getSessionCookieName();
		if(request!=null && request.getCookies()!=null) {
			for(Cookie c : request.getCookies()) {
				if(c.getName().compareToIgnoreCase(sessionCookieName)==0) {
					out.add(c.getValue());
				}
			}
		}
		return out;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(log.isDebugEnabled()){ log.debug(">>>>>>>>>> PRE-HANDLE"); }
		if(FmsSessionHolder.get()!=null) {
			if(log.isDebugEnabled()){ log.debug(">>>>>>>>>> HEADER AUTH, not intefering ... "); }
			return true;
		}
		FmsSession out = null;
		for(String s : getSessionIds(request, response)) {
			FmsSession session = authenticationService.getSession(s);
			if(session==null) {
				if(log.isDebugEnabled()){ log.debug(">> got a session that doesn't exist: "+s); }
				addCookie(response,s, 0);
			} else if(out == null) {
				if(log.isDebugEnabled()){ log.debug(">> got a valid session: "+s); }
				out = session;
			} else {
				if(log.isDebugEnabled()){ log.debug(">> already have a valid session: "+s); }
				authenticationService.destroySession(session);
				addCookie(response,s, 0);
			}
		}
		if(out == null) {
			out = authenticationService.createSession();
			if(log.isDebugEnabled()){ log.debug(">> nothing found, creating new and adding cookie: "+out.getKey()); }
			addCookie(response,out.getKey(), -1);
		}
		FmsSessionHolder.set(out);
		return true;
	}

	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) throws Exception {
		FmsSession session = FmsSessionHolder.get();
		if(log.isDebugEnabled()){ log.debug("<<< ACTUAL session is: "+session); }
		if(session.getKey()!=null) {
			authenticationService.updateSession(session);
		}
		FmsSessionHolder.clear();
		if(log.isDebugEnabled()){ log.debug("<<<<<<<<<< AFTER-COMPLETION"); }
	}


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3) throws Exception {
	}

}
