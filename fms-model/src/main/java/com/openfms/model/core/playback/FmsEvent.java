package com.openfms.model.core.playback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.Status;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;
import com.openfms.model.core.references.FmsDeviceReference;
import com.openfms.model.utils.StatusCombine;

@Indexes(
		{
			@Index(fields={"locationId"},name="idxLocationId",unique=false),
			@Index(fields={"category"},name="idxCategory",unique=false),
			@Index(fields={"startTime"},name="idxStartTime",unique=false),
			@Index(fields={"endTime"},name="idxEndTime",unique=false),

			@Index(fields={"versionStatus"},name="idxVersionStatus",unique=false),
			@Index(fields={"encryptionStatus"},name="idxEncryptionStatus",unique=false),
			@Index(fields={"playbackStatus"},name="idxPlaybackStatus",unique=false),
			@Index(fields={"status"},name="idxStatus",unique=false),

			@Index(fields={"eventItems.movieId"},name="idxMovieId",unique=false),
			@Index(fields={"eventItems.movieVersionId"},name="idxMovieVersionId",unique=false),
			@Index(fields={"name"},name="fe_name", unique=false),
		}
	)
@FmsAccessControlled(FmsAccessControlled.GROUP.SCHEDULE)
public class FmsEvent extends AbstractFmsObject implements Comparable {

	private static final long serialVersionUID = -237225549702642849L;

	private Date startTime;
	private Date endTime;
	private String category;
	private String locationId;
	private String externalLocationId;
	private String locationName;
	private String externalId;
	private String eventRemarks;
	
	private int mediaStatus=Status.ERROR.value();
	private int versionStatus=Status.ERROR.value();
	private int encryptionStatus=Status.ERROR.value();
	private int playbackStatus=Status.ERROR.value();

	private List<FmsError> errors = new ArrayList<FmsError>();
	
	private int length;
	private int implicitLength;
	
	private List<FmsDeviceReference> devices;

	private List<String> tags = new ArrayList<String>();
	
	private List<FmsEventItem> eventItems = new ArrayList<FmsEventItem>();
	
	public FmsEvent() {
		super(null);
	}

	public FmsEvent(String id) {
		super(id);
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public List<FmsEventItem> getEventItems() {
		return eventItems;
	}

	public void setEventItems(List<FmsEventItem> eventItems) {
		this.eventItems = eventItems;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getStatus() {
		return StatusCombine.combine(getVersionStatus(),getEncryptionStatus(),getMediaStatus(),getPlaybackStatus());
	}

	public void setStatus(int status) {
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getEventRemarks() {
		return eventRemarks;
	}

	public void setEventRemarks(String eventRemarks) {
		this.eventRemarks = eventRemarks;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		if(tags==null) {
			this.tags.clear();
			return;
		}
		this.tags = tags;
	}

	@Override
	public String getSearchable() {
		String out = concat(getName(),getExternalId());
		return out;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getImplicitLength() {
		return implicitLength;
	}

	public void setImplicitLength(int implicitLength) {
		this.implicitLength = implicitLength;
	}

	public List<FmsDeviceReference> getDevices() {
		return devices;
	}

	public void setDevices(List<FmsDeviceReference> devices) {
		this.devices = devices;
	}

	@Override
	public int compareTo(Object o) {
		if(!(o instanceof FmsEvent)) {
			return super.compareTo(o);
		}
		return this.getStartTime().compareTo(((FmsEvent)o).getStartTime());
	}

	public int getMediaStatus() {
		return mediaStatus;
	}

	public void setMediaStatus(int mediaStatus) {
		this.mediaStatus = mediaStatus;
	}

	public int getVersionStatus() {
		return versionStatus;
	}

	public void setVersionStatus(int versionStatus) {
		this.versionStatus = versionStatus;
	}

	public int getEncryptionStatus() {
		return encryptionStatus;
	}

	public void setEncryptionStatus(int encryptionStatus) {
		this.encryptionStatus = encryptionStatus;
	}

	public int getPlaybackStatus() {
		return playbackStatus;
	}

	public void setPlaybackStatus(int playbackStatus) {
		this.playbackStatus = playbackStatus;
	}

	public List<FmsError> getErrors() {
		return errors;
	}

	public void setErrors(List<FmsError> errors) {
		this.errors = errors;
	}

	public void addError(int status, String message) {
		if(this.errors==null) {
			this.errors = new ArrayList<FmsError>();
		}
		FmsError e = new FmsError();
		e.setMessage(message);
		e.setStatus(status);
		this.errors.add(e);
	}

	public String getExternalLocationId() {
		return externalLocationId;
	}

	public void setExternalLocationId(String externalLocationId) {
		this.externalLocationId = externalLocationId;
	}
	

}
