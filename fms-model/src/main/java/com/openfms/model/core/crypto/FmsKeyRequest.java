package com.openfms.model.core.crypto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.KEY_REQUESTS)
public class FmsKeyRequest extends AbstractFmsObject {

	private static final long serialVersionUID = -3231692091334973530L;

	private String eventId;
	private String mediaClipId;
	private String mediaClipExternalId;
	private String mediaClipName;
	private String deviceId;
	private String deviceName;
	private String certificateId;
	private String certificateDnQualifier;
	private String locationId;
	private String locationName;
	private String status;

	private Date start;
	private Date end;
	
	private String movieId;
	private String movieName;
	private String keyId;
	private List<String> keyIds = new ArrayList<String>();
	
	private boolean canceled = false;
	
	private String createdBy;
	private String reason;
	private String requestedBy;
	private boolean fulfilled = false;
	private String fulfilledBy;
	private List<String> tdlThumbprints = new ArrayList<String>();
	
	public String getMediaClipId() {
		return mediaClipId;
	}

	public void setMediaClipId(String mediaClipId) {
		this.mediaClipId = mediaClipId;
	}

	public String getMediaClipExternalId() {
		return mediaClipExternalId;
	}

	public void setMediaClipExternalId(String mediaClipExternalId) {
		this.mediaClipExternalId = mediaClipExternalId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public String getCertificateDnQualifier() {
		return certificateDnQualifier;
	}

	public void setCertificateDnQualifier(String certificateDnQualifier) {
		this.certificateDnQualifier = certificateDnQualifier;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		if(!this.canceled && canceled) {
			this.canceled = canceled;
		}
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getFulfilledBy() {
		return fulfilledBy;
	}

	public void setFulfilledBy(String fulfilledBy) {
		this.fulfilledBy = fulfilledBy;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getMediaClipName() {
		return mediaClipName;
	}

	public void setMediaClipName(String mediaClipName) {
		this.mediaClipName = mediaClipName;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public List<String> getKeyIds() {
		return keyIds;
	}

	public void setKeyIds(List<String> keyIds) {
		this.keyIds = keyIds;
	}

	public boolean isFulfilled() {
		return fulfilled;
	}

	public void setFulfilled(boolean fulfilled) {
		this.fulfilled = fulfilled;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getTdlThumbprints() {
		return tdlThumbprints;
	}

	public void setTdlThumbprints(List<String> tdlThumbprints) {
		this.tdlThumbprints = tdlThumbprints;
	}


}
