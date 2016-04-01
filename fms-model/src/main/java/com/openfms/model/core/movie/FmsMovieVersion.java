package com.openfms.model.core.movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;
import com.openfms.model.core.references.FmsEventReference;
import com.openfms.model.core.references.FmsMediaClipReference;


@Indexes(
		{
			@Index(fields={"externalId","projectId"},name="movieVersionUniqueExternalId",unique=true),
			@Index(fields={"mediaClipIds"},name="idxmediaClipIds"),
			@Index(fields={"movieId"},name="idxMovieId"),
			@Index(fields={"disabled"},name="fmp_disabled ", unique=false)
		}
	)
@FmsAccessControlled(FmsAccessControlled.GROUP.MOVIE)
public class FmsMovieVersion extends AbstractFmsObject {

	private static final long serialVersionUID = -5989836882174772655L;

	private String movieId;
	private String replacedBy;
	private String externalId;
	
	private int status = -1;
	private boolean disabled=false;
	
	private List<String> mediaClipIds = new ArrayList<String>();
	private List<FmsMediaClipReference> mediaClips = new ArrayList<FmsMediaClipReference>();
	
	private List<String> audioLanguageIds = new ArrayList<String>();
	private List<String> subtitleLanguageIds = new ArrayList<String>();
	
	private List<FmsEventReference> events = new ArrayList<FmsEventReference>();
	private List<String> tags;
	
	private Date firstEventDate;
	private Date lastEventDate;
	
	private int length = -1;

	public FmsMovieVersion() {
		super(null);
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public List<String> getMediaClipIds() {
		return mediaClipIds;
	}

	public void setMediaClipIds(List<String> mediaClipIds) {
		this.mediaClipIds = mediaClipIds;
	}

	public List<String> getAudioLanguageIds() {
		return audioLanguageIds;
	}

	public void setAudioLanguageIds(List<String> audioLanguageIds) {
		this.audioLanguageIds = audioLanguageIds;
	}

	public List<String> getSubtitleLanguageIds() {
		return subtitleLanguageIds;
	}

	public void setSubtitleLanguageIds(List<String> subtitleLanguageIds) {
		this.subtitleLanguageIds = subtitleLanguageIds;
	}

	public String getReplacedBy() {
		return replacedBy;
	}

	public void setReplacedBy(String replacedBy) {
		this.replacedBy = replacedBy;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getDisplayName() {
		StringBuffer sb = new StringBuffer();
		if(getExternalId()!=null && getExternalId().length()>0) {
			sb.append('[');
			sb.append(getExternalId());
			sb.append(']');
			sb.append(' ');
		}
		sb.append("[");
		if(getAudioLanguageIds()!=null && getAudioLanguageIds().size()>0) {
			for(String s : getAudioLanguageIds()) {
				sb.append(s);
				sb.append(' ');
			}
		} else {
			sb.append("none");
		}
		sb.append("][");
		if(getSubtitleLanguageIds()!=null && getSubtitleLanguageIds().size()>0) {
			for(String s : getSubtitleLanguageIds()) {
				sb.append(s);
				sb.append(' ');
			}
		} else {
			sb.append("none");
		}
		sb.append(']');
		sb.append(getName());
		return sb.toString();
	}
	
	public void setDisplayName(String s) {
	}

	public Date getFirstEventDate() {
		return firstEventDate;
	}

	public void setFirstEventDate(Date firstEventDate) {
		this.firstEventDate = firstEventDate;
	}

	public Date getLastEventDate() {
		return lastEventDate;
	}

	public void setLastEventDate(Date lastEventDate) {
		this.lastEventDate = lastEventDate;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<FmsEventReference> getEvents() {
		if(events == null) {
			events = new ArrayList<FmsEventReference>();
		}
		return events;
	}

	public void setEvents(List<FmsEventReference> events) {
		this.events.clear();
		if(events!=null) {
			this.events.addAll(events);
		}
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	
	public String getSignature() {
		StringBuffer sb = new StringBuffer();
		sb.append("Audio: ");
		if(getAudioLanguageIds()!=null) {
			sb.append(StringUtils.join(getAudioLanguageIds().iterator(),", "));
		} else {
			sb.append("none");
		}
		sb.append(", Subtitles: ");
		if(getSubtitleLanguageIds()!=null) {
			sb.append(StringUtils.join(getSubtitleLanguageIds().iterator(),", "));
		} else {
			sb.append("none");
		}
		return sb.toString();
	}
	
	public void setSignature(String s) {
		
	}

	public List<FmsMediaClipReference> getMediaClips() {
		return mediaClips;
	}

	public void setMediaClips(List<FmsMediaClipReference> mediaClips) {
		this.mediaClips = mediaClips;
	}
	


}
