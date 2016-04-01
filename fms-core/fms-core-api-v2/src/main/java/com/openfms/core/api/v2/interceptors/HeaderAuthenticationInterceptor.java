package com.openfms.core.api.v2.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.openfms.core.service.AuthnService;
import com.openfms.model.core.auth.FmsAuthentication;
import com.openfms.model.core.auth.FmsSession;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.project.FmsProjectHolder;

public class HeaderAuthenticationInterceptor  extends HandlerInterceptorAdapter {
	
	private static Log log = LogFactory.getLog(ProjectInterceptor.class);

	@Autowired
	private AuthnService authnService;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String username = request.getHeader("x-fms-auth-username");
		String email = request.getHeader("x-fms-auth-email");
		String password = request.getHeader("x-fms-auth-password");
		FmsAuthentication auth = null;
		if(username!=null && password!=null) {
			auth = new FmsAuthentication(null,username,password);
		} else if(email!=null && password!=null) {
			auth = new FmsAuthentication(email,null,password);
		} else {
			return true;
		}
		try {
			FmsUser user = authnService.authenticate(auth);
			if(user != null) {
				FmsSession session = new FmsSession(); 
				session.setUser(user);
				FmsSessionHolder.set(session);
				return true;
			}
		} catch (Exception e) {
			log.error(" ---> error in header authentication", e);
		}
		response.sendError(403,"login failed!");
		return false;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		FmsProjectHolder.clear();
	}

}
