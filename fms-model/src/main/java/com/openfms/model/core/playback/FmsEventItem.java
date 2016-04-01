package com.openfms.model.core.playback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.openfms.model.Status;
import com.openfms.model.core.references.FmsMediaClipReference;
import com.openfms.model.utils.StatusCombine;


public class FmsEventItem {

	private String name;
	private String externalId;
	private String movieExternalId;
	private String movieId;
	private String movieName;
	private String movieVersionId;
	private String movieVersionName;
	private int length = -1;
	private int mediaStatus=Status.ERROR.value();
	private int versionStatus=Status.ERROR.value();
	private int encryptionStatus=Status.ERROR.value();
	private int playbackStatus=Status.ERROR.value();
	
	private List<String> keyIds = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();
	private List<String> audioLanguageIds = new ArrayList<String>();
	private List<String> subtitleLanguageIds = new ArrayList<String>();
	
	private List<FmsMediaClipReference> mediaClips = new ArrayList<FmsMediaClipReference>();

    public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getMovieVersionId() {
		return movieVersionId;
	}

	public void setMovieVersionId(String movieVersionId) {
		this.movieVersionId = movieVersionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public int getStatus() {
		return StatusCombine.combine(getVersionStatus(),getMediaStatus(),getEncryptionStatus(),getPlaybackStatus());
	}

	public void setStatus(int status) {
	}

	public List<String> getAudioLanguageIds() {
		return audioLanguageIds!=null?audioLanguageIds:new ArrayList<String>();
	}

	public void setAudioLanguageIds(List<String> audioLanguageIds) {
		this.audioLanguageIds = audioLanguageIds;
	}

	public List<String> getSubtitleLanguageIds() {
		return subtitleLanguageIds!=null?subtitleLanguageIds:new ArrayList<String>();
	}

	public void setSubtitleLanguageIds(List<String> subtitleLanguageIds) {
		this.subtitleLanguageIds = subtitleLanguageIds;
	}

	public int getVersionStatus() {
		return versionStatus;
	}

	public void setVersionStatus(int versionStatus) {
		this.versionStatus = versionStatus;
	}

	public List<String> getTags() {
		if(tags==null) {
			tags = new ArrayList<>();
		}
		return Collections.unmodifiableList(tags);
	}

	public void addTag(String tag) {
		tags = tags==null?new ArrayList<String>():tags;
		if(!tags.contains(tag)) {
			tags.add(tag);
		}
	}
	
	public void setTags(List<String> tags) {
		this.tags = new ArrayList<>();
		if(tags!=null) {
			for(String t : tags) {
				addTag(t);
			}
		}
	}

	public String getMovieExternalId() {
		return movieExternalId;
	}

	public void setMovieExternalId(String movieExternalId) {
		this.movieExternalId = movieExternalId;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<FmsMediaClipReference> getMediaClips() {
		return mediaClips;
	}

	public void setMediaClips(List<FmsMediaClipReference> mediaClips) {
		this.mediaClips = mediaClips;
	}

	public int getMediaStatus() {
		return mediaStatus;
	}

	public void setMediaStatus(int mediaStatus) {
		this.mediaStatus = mediaStatus;
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
		List<String> newKeyIds = new ArrayList<String>();
		if(keyIds!=null) {
			newKeyIds.addAll(keyIds);
		}
		this.keyIds = newKeyIds;
	}

	public String getMovieVersionName() {
		return movieVersionName;
	}

	public void setMovieVersionName(String movieVersionName) {
		this.movieVersionName = movieVersionName;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
}
