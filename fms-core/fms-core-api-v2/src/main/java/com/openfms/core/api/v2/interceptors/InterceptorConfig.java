package com.openfms.core.api.v2.interceptors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
	
	@Bean
	public ProjectInterceptor projectInterceptor() {
		return new ProjectInterceptor();
	}

	@Bean
	public HeaderAuthenticationInterceptor headerAuthInterceptor() {
		return new HeaderAuthenticationInterceptor();
	}
	
	@Bean
	public SessionInterceptor sessionInterceptor() {
		return new SessionInterceptor();
	}
	
	@Bean
	public ClearListenerTrackerInterceptor clearListenerTrackerInterceptor() {
		return new ClearListenerTrackerInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(projectInterceptor());
		registry.addInterceptor(headerAuthInterceptor());
		registry.addInterceptor(sessionInterceptor());
		registry.addInterceptor(clearListenerTrackerInterceptor());
	}
	
}
