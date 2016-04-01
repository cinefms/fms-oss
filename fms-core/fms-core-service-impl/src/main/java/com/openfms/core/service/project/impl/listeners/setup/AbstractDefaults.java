package com.openfms.core.service.project.impl.listeners.setup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectHolder;
import com.openfms.model.utils.ListenerTrackerHolder;

public abstract class AbstractDefaults<T extends FmsObject> extends FmsListenerAdapter<FmsProject> {

	public static Log log = LogFactory.getLog(AbstractDefaults.class);
	
	public abstract String getFilename();
	
	public abstract Class<T> getDefaultType();
	
	@Autowired
	private ProjectDataStore dataStore;
	
	
	@Override
	protected void created(String db, FmsProject project) {
		FmsProjectHolder.set(project);
		ListenerTrackerHolder.clear();
		InputStream is = null;
		try {
			log.info("importing defaults for:  "+this.getClass());
			log.info("importing defaults from: "+getFilename());
			ObjectMapper m = new ObjectMapper();
			m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			is = this.getClass().getClassLoader().getResourceAsStream(getFilename());
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String s = null;
			List<T> all = new ArrayList<>();
			while((s = br.readLine()) != null) {
				all.add(m.readValue(s, getDefaultType()));
			}
			int count = 0;
			for(T t : all) {
				count++;
				log.info("importing default: "+getDefaultType()+": "+count+" / "+all.size());
				dataStore.saveDirect(t);
			}
		} catch (Exception e) {
			log.error("error importing defaults for: "+this.getClass(),e);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public boolean supports(Class<? extends FmsObject> clazz) {
		return clazz == FmsProject.class;
	}
	
	
	
}
