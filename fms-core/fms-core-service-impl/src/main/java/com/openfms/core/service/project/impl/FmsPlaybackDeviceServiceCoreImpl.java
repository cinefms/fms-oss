package com.openfms.core.service.project.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsPlaybackDeviceService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.config.FmsCredentials;
import com.openfms.model.core.playback.FmsCinemaServerStatus;
import com.openfms.model.core.playback.FmsCinemaServerStatusBrief;
import com.openfms.model.core.playback.FmsDeviceProtocol;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.core.playback.connector.ConnectorFactory;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

@Service
public class FmsPlaybackDeviceServiceCoreImpl extends GenericProjectServiceImpl<FmsPlaybackDevice> implements FmsPlaybackDeviceService {

	@Autowired
	private ApplicationContext ctx;
	
	
	public FmsPlaybackDeviceServiceCoreImpl() {
	}

	
	@Override
	public void saveCredentials(String playbackDeviceId, FmsCredentials c) throws AccessDeniedException, DatabaseException, VersioningException, EntityNotFoundException {
		if(!authzService.allowAccess(FmsPlaybackDevice.class, playbackDeviceId, AccessType.ADMIN)) {
			throw new AccessDeniedException();
		}
		FmsPlaybackDevice pd = get(playbackDeviceId);
		pd.setUsername(c.getUsername());
		save(pd);
		c.setId(pd.getId());
		dataStore.saveObject(c);
	}
	
	@Override
	public FmsCredentials getCredentials(String playbackDeviceId) throws AccessDeniedException, DatabaseException {
		if(!authzService.allowAccess(FmsPlaybackDevice.class, playbackDeviceId, AccessType.ADMIN)) {
			throw new AccessDeniedException();
		}
		FmsCredentials out = dataStore.getObject(FmsCredentials.class, playbackDeviceId);
		return out;
	}
	
	@Override
	public FmsDeviceProtocol getPlaybackDeviceProtocol(String playbackDeviceProtocolId) {
		for(FmsDeviceProtocol dp : listPlaybackDeviceProtocols(null)) {
			if(dp.getId().compareTo(playbackDeviceProtocolId)==0) {
				return dp;
			}
		}
		return null;
	}

	@Override
	public List<FmsDeviceProtocol> listPlaybackDeviceProtocols(String searchTerm) {
		List<ConnectorFactory> connectorFactories = new ArrayList<ConnectorFactory>(ctx.getBeansOfType(ConnectorFactory.class).values());
		List<String> names = new ArrayList<String>();
		for(ConnectorFactory cf : connectorFactories) {
			names.add(cf.getSupportedProtocol());
		}
		Collections.sort(names);
		List<FmsDeviceProtocol> out = new ArrayList<FmsDeviceProtocol>();
		for(String s : names) {
			if(searchTerm == null || s.toUpperCase().contains(searchTerm.toUpperCase())) {
				out.add(new FmsDeviceProtocol(s));
			}
		}
		return out;
	}
	
	
	@Override
	public FmsCinemaServerStatus savePlaybackDeviceStatus(FmsCinemaServerStatus status) throws AccessDeniedException, DatabaseException, VersioningException {
		if(!authzService.allowAccess(FmsPlaybackDevice.class, AccessType.UPDATE)) {
			throw new AccessDeniedException();
		}
		return dataStore.saveObject(status);
	}
	
	@Override
	public List<FmsCinemaServerStatusBrief> getCinemaServerStatus(String deviceId, String order, boolean asc, int start, int max) throws DatabaseException, AccessDeniedException {
		if(!authzService.allowAccess(FmsPlaybackDevice.class, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		List<FmsCinemaServerStatusBrief> out = new ArrayList<FmsCinemaServerStatusBrief>();
		DBStoreQuery q = BasicQuery.createQuery();
		q = q.eq("deviceId", deviceId);
		q = q.order(order,asc);
		q = q.max(max);
		q = q.start(start);
		for(FmsCinemaServerStatus css : dataStore.findObjects(FmsCinemaServerStatus.class, q)) {
			out.add(css.getBrief());
		}
		return out;
	}
	
	@Override
	public FmsCinemaServerStatusBrief getCinemaServerStatus(String deviceId, String statusId) throws DatabaseException, AccessDeniedException {
		return getCinemaServerStatusDetail(deviceId, statusId).getBrief();
	}

	@Override
	public FmsCinemaServerStatus getCinemaServerStatusDetail(String deviceId, String statusId) throws DatabaseException, AccessDeniedException {
		if(!authzService.allowAccess(FmsPlaybackDevice.class, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		return dataStore.getObject(FmsCinemaServerStatus.class, statusId);
	}
	
	
	
}
