package com.openfms.core.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.cinefms.dbstore.api.DataStore;
import com.cinefms.dbstore.utils.mongo.MongoDataStore;
import com.cinefms.dbstore.utils.mongo.MongoService;
import com.openfms.core.service.SessionStore;
import com.openfms.core.service.impl.InMemorySessionStore;

@Configuration
@PropertySource(value={"service-impl-config.properties"})
public class DbConfig {

	@Bean
	public static SessionStore sessionStore() {
		return new InMemorySessionStore();
	}
	
	@Bean
	public static DataStore dataStore(MongoService mongoService, @Value("${dbPrefix}") String prefix) {
		MongoDataStore out = new MongoDataStore();
		out.setMongoService(mongoService);
		out.setDefaultDb(prefix);
		out.setDbPrefix(prefix+"_");
		return out;
	}

	@Bean
	public static MongoService mongoService(@Value("${mongo.host}") String mongoHost, @Value("${mongo.port}") int mongoPort) {
		MongoService mongoService = new MongoService();
		mongoService.setHosts(mongoHost+":"+mongoPort);
		return mongoService;
	}
	
	
}
