package com.openfms.core.api.v2.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.openfms.model.utils.ListenerTrackerHolder;

public class ClearListenerTrackerInterceptor extends HandlerInterceptorAdapter {

	private static Log log = LogFactory.getLog(ClearListenerTrackerInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		ListenerTrackerHolder.clear();
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		ListenerTrackerHolder.clear();
	}
	
}
