package com.openfms.model.core.project;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.PROJECT)
public class FmsMailServerConfig extends AbstractFmsObject {

	private static final long serialVersionUID = 6611385526997618862L;

	private boolean useSsl;
	private boolean useTls;
	private String host;
	private int port;
	private String username;

	public boolean isUseSsl() {
		return useSsl;
	}

	public void setUseSsl(boolean useSsl) {
		this.useSsl = useSsl;
	}

	public boolean isUseTls() {
		return useTls;
	}

	public void setUseTls(boolean useTls) {
		this.useTls = useTls;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
