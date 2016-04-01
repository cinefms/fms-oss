package com.openfms.model.core.movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.annotations.Searchable;
import com.openfms.model.core.AbstractFmsObject;
import com.openfms.model.core.global.FmsComment;
import com.openfms.model.core.references.FmsEventReference;

@Indexes(
		{
			@Index(fields={"externalId","projectId"},name="movieUniqueExternalId",unique=false),
			@Index(fields={"movieStatus"},name="idxMovieStatus",unique=false),
			@Index(fields={"name"},name="fmp_name", unique=false)
		}
	)
@FmsAccessControlled(FmsAccessControlled.GROUP.MOVIE)
public class FmsMovie extends AbstractFmsObject {
	
	private static final long serialVersionUID = -4008578199553765474L;

	private String externalId;
	private String director;
	private String category;
	private String contact;
	private String contactUserId;
	private List<FmsComment> comments = new ArrayList<FmsComment>();
	private List<FmsEventReference> events = new ArrayList<FmsEventReference>();
	
	
	private int numEvents;
	private int numVersion;
	private int numClips;
	
	private int openTasks;
	private int mediaStatus;
	private int versionStatus;
	private int encryptionStatus;
	private int playbackStatus;
	private Date firstEventDate;

	private Date nextEventDate;
	private String nextEventId;
	
	private int movieStatus;
	
	private List<String> countryIds = new ArrayList<String>();
	private List<String> languageIds = new ArrayList<String>();
	
	private int length=-1;

	public FmsMovie() {
		super(null);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Searchable
	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactUserId() {
		return contactUserId;
	}

	public void setContactUserId(String contactUserId) {
		this.contactUserId = contactUserId;
	}

	public List<FmsComment> getComments() {
		return comments;
	}

	public void setComments(List<FmsComment> comments) {
		if(comments==null) {
			this.comments.clear();
			return;
		}
		this.comments = comments;
	}

	public List<String> getCountryIds() {
		return countryIds;
	}

	public void setCountryIds(List<String> countryIds) {
		if(countryIds == null) {
			this.countryIds.clear();
			return;
		}
		this.countryIds = countryIds;
	}

	public List<String> getLanguageIds() {
		return languageIds;
	}

	public void setLanguageIds(List<String> languageIds) {
		if(languageIds == null) {
			this.languageIds.clear();
			return;
		}
		this.languageIds = languageIds;
	}

	public int getMovieStatus() {
		return movieStatus;
	}

	public void setMovieStatus(int movieStatus) {
		this.movieStatus = movieStatus;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<FmsEventReference> getEvents() {
		if(events == null) {
			events = new ArrayList<FmsEventReference>();
		}
		return events;
	}

	public void setEvents(List<FmsEventReference> events) {
		if(events==null) {
			this.events.clear();
		}
		this.events = events;
	}

	public int getOpenTasks() {
		return openTasks;
	}

	public void setOpenTasks(int openTasks) {
		this.openTasks = openTasks;
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

	@Override
	public String getSearchable() {
		return concat(getExternalId(),getDirector(),getName());
	}

	public int getMediaStatus() {
		return mediaStatus;
	}

	public void setMediaStatus(int mediaStatus) {
		this.mediaStatus = mediaStatus;
	}

	public int getPlaybackStatus() {
		return playbackStatus;
	}

	public void setPlaybackStatus(int playbackStatus) {
		this.playbackStatus = playbackStatus;
	}

	public Date getFirstEventDate() {
		return firstEventDate;
	}

	public void setFirstEventDate(Date firstEventDate) {
		this.firstEventDate = firstEventDate;
	}

	public int getNumEvents() {
		return numEvents;
	}

	public void setNumEvents(int numEvents) {
		this.numEvents = numEvents;
	}

	public int getNumVersion() {
		return numVersion;
	}

	public void setNumVersion(int numVersion) {
		this.numVersion = numVersion;
	}

	public int getNumClips() {
		return numClips;
	}

	public void setNumClips(int numClips) {
		this.numClips = numClips;
	}

	public Date getNextEventDate() {
		return nextEventDate;
	}

	public void setNextEventDate(Date nextEventDate) {
		this.nextEventDate = nextEventDate;
	}

	public String getNextEventId() {
		return nextEventId;
	}

	public void setNextEventId(String nextEventId) {
		this.nextEventId = nextEventId;
	}

	
}
