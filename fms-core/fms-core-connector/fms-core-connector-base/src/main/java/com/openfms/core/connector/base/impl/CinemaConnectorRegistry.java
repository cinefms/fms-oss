package com.openfms.core.connector.base.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.openfms.model.core.playback.connector.ConnectorFactory;

@Component
public class CinemaConnectorRegistry {

	private static Map<String, ConnectorFactory> factories = new HashMap<String, ConnectorFactory>();

	@Autowired
	private ApplicationContext applicationContext;

	@PostConstruct
	public void mapConnectors() {
		for (ConnectorFactory cf : applicationContext.getBeansOfType(
				ConnectorFactory.class).values()) {
			factories.put(cf.getSupportedProtocol(), cf);
		}
	}

	public static ConnectorFactory getFactory(String protocol) {
		return factories.get(protocol);
	}

	public static void addFactory(ConnectorFactory cf) {
		factories.put(cf.getSupportedProtocol(), cf);
	}
	
}
