package com.openfms.core.service.project.impl.listeners;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.playback.FmsCinemaServerCplStatus;
import com.openfms.model.core.playback.FmsCinemaServerStatus;

@Component
public class FmsCinemaServerStatusListener extends FmsListenerAdapter<FmsCinemaServerStatus> {

	@Autowired
	private FmsMediaClipService mediaClipService; 
	
	@Override
	protected boolean beforeSave(String db, FmsCinemaServerStatus object) {
		if(object.getCplStatus()!=null) {
			for(FmsCinemaServerCplStatus cplStatus : object.getCplStatus()) {
				if(cplStatus.getCplUuid()!=null) {
					try {
						DBStoreQuery q = BasicQuery.createQuery().eq("externalId", cplStatus.getCplUuid());
						List<FmsMediaClip> cp = mediaClipService.list(q);
						if(cp.size()>0) {
							cplStatus.setMediaClipId(cp.get(0).getId());
						}
					} catch (Exception e) {
						// ignore ...
					}
				}
			}
		}
		return true;
	}

}
