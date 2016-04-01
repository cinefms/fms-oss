package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.config.FmsCredentials;
import com.openfms.model.core.playback.FmsCinemaServerStatus;
import com.openfms.model.core.playback.FmsCinemaServerStatusBrief;
import com.openfms.model.core.playback.FmsDeviceProtocol;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

public interface FmsPlaybackDeviceService extends GenericService<FmsPlaybackDevice> {

	public FmsCredentials getCredentials(String playbackDeviceId) throws AccessDeniedException, DatabaseException;

	public void saveCredentials(String playbackDeviceId, FmsCredentials c) throws AccessDeniedException, DatabaseException, VersioningException, EntityNotFoundException;

	public List<FmsDeviceProtocol> listPlaybackDeviceProtocols(String searchTerm);

	public FmsDeviceProtocol getPlaybackDeviceProtocol(String playbackDeviceProtocolId);

	public List<FmsCinemaServerStatusBrief> getCinemaServerStatus(String deviceId, String order, boolean asc, int start, int max) throws DatabaseException, AccessDeniedException;

	public FmsCinemaServerStatusBrief getCinemaServerStatus(String deviceId, String statusId) throws DatabaseException, AccessDeniedException;
	
	public FmsCinemaServerStatus getCinemaServerStatusDetail(String deviceId, String statusId) throws DatabaseException, AccessDeniedException;

	public FmsCinemaServerStatus savePlaybackDeviceStatus(FmsCinemaServerStatus status) throws AccessDeniedException, DatabaseException, VersioningException;

}
