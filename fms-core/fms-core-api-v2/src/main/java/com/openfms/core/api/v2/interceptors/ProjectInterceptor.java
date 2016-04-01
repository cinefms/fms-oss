package com.openfms.core.api.v2.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.openfms.core.api.v2.AdminController;
import com.openfms.core.service.admin.ProjectService;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectHolder;

public class ProjectInterceptor extends HandlerInterceptorAdapter {
	
	private static Log log = LogFactory.getLog(ProjectInterceptor.class);

	@Autowired
	private ProjectService projectService;
	
	private boolean abort = true;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(log.isDebugEnabled()){ log.debug(">>>>>>>>>> PRE-HANDLE"); }
		if(handler instanceof AdminController) {
			return true;
		}
		try {
			FmsProject prj = null;
			String host = request.getHeader("Host");
			if(host.indexOf(":")>-1) {
				host = host.substring(0, host.indexOf(":"));
			}
			prj = projectService.getProjectForHost(host);
			if(prj != null && prj.isActive()) {
				if(log.isDebugEnabled()){ log.debug("project for: "+host+" / "+prj.getName()); }
				FmsProjectHolder.set(prj);
				return true;
			}
			log.error("unknown project: "+host+" (request from: "+request.getRemoteAddr()+") ");
		} catch (Exception e) {
			log.error("error finding project (with project service: "+projectService+") ",e);
		}
		return !isAbort();
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		FmsProjectHolder.clear();
	}

	public boolean isAbort() {
		return abort;
	}

	public void setAbort(boolean abort) {
		this.abort = abort;
	}

}
