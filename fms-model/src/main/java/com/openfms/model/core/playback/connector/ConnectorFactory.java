package com.openfms.model.core.playback.connector;

import java.io.IOException;
import java.util.Properties;

public interface ConnectorFactory {

	
	public String getSupportedProtocol();
	
	
	public CinemaServerConnector connect(String protocol, String host, int port, String username, String password, Properties properties) throws IOException;
	
}
