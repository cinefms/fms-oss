package com.openfms.model.core.playback.connector;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.openfms.model.core.playback.FmsCinemaServerCplStatus;
import com.openfms.model.core.playback.FmsCinemaServerDiskStatus;

public interface CinemaServerConnector {
	
	public abstract FmsCinemaServerDiskStatus getStatus() throws IOException;
	
	public abstract List<String> getKdmUuids() throws IOException;
	
	public abstract void uploadKdm(byte[] kdms, String filename) throws IOException;

	public abstract List<String> getCplUuids() throws IOException;

	public abstract void setError(boolean error);

	public abstract boolean isError();

	public abstract FmsCinemaServerCplStatus getCplStatus(String s) throws IOException;

	public abstract boolean validateCpl(String cplUuid, Date time) throws IOException;

	public abstract void close();
	
	
}
