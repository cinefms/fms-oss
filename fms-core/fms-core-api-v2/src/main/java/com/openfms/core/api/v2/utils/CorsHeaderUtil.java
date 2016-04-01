package com.openfms.core.api.v2.utils;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;

public class CorsHeaderUtil {

	public static final String HTTP_HEADER_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String HTTP_HEADER_ALLOW = "Allow";

	public static void setCorsAllowedMethodHeaders(HttpServletResponse response, List<HttpMethod> methods) {
		StringBuffer sb = new StringBuffer();
		for(HttpMethod m : methods) {
			sb.append(m.name());
			sb.append(" ");
		}
		String h = sb.toString().trim().replace(' ', ',');
		response.setHeader(HTTP_HEADER_ALLOW, h);
		response.setHeader(HTTP_HEADER_ACCESS_CONTROL_ALLOW_METHODS, h);
	}
	
	
	
}
