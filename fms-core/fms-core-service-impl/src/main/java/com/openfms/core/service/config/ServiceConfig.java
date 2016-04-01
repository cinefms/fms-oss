package com.openfms.core.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openfms.core.service.AuthnService;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.admin.impl.auth.AdminCanDoAnythingAuthzProvider;
import com.openfms.core.service.admin.impl.auth.AdminUserAuthnProvider;
import com.openfms.core.service.impl.AuthnServiceImpl;
import com.openfms.core.service.impl.AuthzServiceImpl;
import com.openfms.core.service.project.auth.impl.FmsProjectUserGroupAuthzProvider;
import com.openfms.core.service.project.auth.impl.LoggedInUserBasicAuthzProvider;
import com.openfms.core.service.project.auth.impl.MovieAccessAuthzProvider;
import com.openfms.core.service.project.impl.FmsProjectUserServiceImpl;
import com.openfms.core.service.util.AuthnProvider;

@Configuration
public class ServiceConfig {



	
}
