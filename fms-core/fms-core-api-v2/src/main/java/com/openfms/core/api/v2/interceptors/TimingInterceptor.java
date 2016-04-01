package com.openfms.core.api.v2.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.skjlls.aspects.metrics.MetricsService;

public class TimingInterceptor implements HandlerInterceptor {

	
	@Autowired
	private MetricsService metricsService;
	
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		if(arg2 instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod)arg2;
			String name = "api.v2."+hm.getBean().getClass().getSimpleName()+"."+hm.getMethod().getName();
			//metricsService.event(name);
			Long l = (Long)arg0.getAttribute("__http__start");
			metricsService.timing(System.currentTimeMillis()-l.longValue(), name);
			metricsService.event(name+".status."+arg1.getStatus());
		}
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,	Object arg2) throws Exception {
		arg0.setAttribute("__http__start", System.currentTimeMillis());
		return true;
	}

	public static void main(String[] args) {
	}
	
}
