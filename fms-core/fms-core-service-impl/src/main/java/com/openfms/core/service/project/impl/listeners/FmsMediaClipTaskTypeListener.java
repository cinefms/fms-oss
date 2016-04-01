package com.openfms.core.service.project.impl.listeners;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsPlaybackDeviceService;
import com.openfms.model.core.movie.FmsMediaClipTaskType;
import com.openfms.model.core.playback.FmsPlaybackDevice;

@Component
public class FmsMediaClipTaskTypeListener extends FmsListenerAdapter<FmsMediaClipTaskType> {

	private static Log log = LogFactory.getLog(FmsMediaClipTaskTypeListener.class);

	@Autowired
	private FmsPlaybackDeviceService deviceService;
	
	@Override
	protected boolean beforeSave(String db, FmsMediaClipTaskType taskType) {
		try {
			if(taskType.getDeviceId()!=null) {
				FmsPlaybackDevice d = deviceService.get(taskType.getDeviceId());
				taskType.setDeviceId(d.getId());
				taskType.setDeviceName(d.getName()+" ("+d.getVendor()+")");
			}
		} catch (Exception e) {
			log.error("error updating taskType device",e);
		}
		return true;

	}

}
