package com.openfms.model.core.media;

import java.util.ArrayList;
import java.util.List;

public class FmsMovieReel {

	private List<FmsAudioTrack> audioTracks = new ArrayList<FmsAudioTrack>();
	private List<FmsSubtitleTrack> subtitleTracks = new ArrayList<FmsSubtitleTrack>();
	private List<FmsPictureTrack> pictureTracks = new ArrayList<FmsPictureTrack>();
	private List<String> audioLanguageIds = new ArrayList<String>();
	private List<String> subtitleLanguageIds = new ArrayList<String>();
	private String mediaAspect;
	private String screenAspect;
	private Boolean encrypted;

	public List<FmsAudioTrack> getAudioTracks() {
		return audioTracks;
	}

	public void setAudioTracks(List<FmsAudioTrack> audioTracks) {
		if(audioTracks==null) {
			this.audioTracks.clear();
			return;
		}
		this.audioTracks = audioTracks;
	}

	public List<FmsSubtitleTrack> getSubtitleTracks() {
		return subtitleTracks;
	}

	public void setSubtitleTracks(List<FmsSubtitleTrack> subtitleTracks) {
		if(subtitleTracks==null) {
			this.subtitleTracks.clear();
			return;
		}
		this.subtitleTracks = subtitleTracks;
	}

	public List<FmsPictureTrack> getPictureTracks() {
		return pictureTracks;
	}

	public void setPictureTracks(List<FmsPictureTrack> pictureTracks) {
		if(pictureTracks==null) {
			this.pictureTracks.clear();
			return;
		}
		this.pictureTracks = pictureTracks;
	}

	public List<String> getAudioLanguageIds() {
		return audioLanguageIds;
	}

	public void setAudioLanguageIds(List<String> audioLanguageIds) {
		if(audioLanguageIds==null) {
			this.audioLanguageIds.clear();
			return;
		}
		this.audioLanguageIds = audioLanguageIds;
	}

	public List<String> getSubtitleLanguageIds() {
		return subtitleLanguageIds;
	}

	public void setSubtitleLanguageIds(List<String> subtitleLanguageIds) {
		if(subtitleLanguageIds==null) {
			this.subtitleLanguageIds.clear();
			return;
		}
		this.subtitleLanguageIds = subtitleLanguageIds;
	}

	public Boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(Boolean encrypted) {
		this.encrypted = encrypted;
	}

	public String getMediaAspect() {
		return mediaAspect;
	}

	public void setMediaAspect(String mediaAspect) {
		this.mediaAspect = mediaAspect;
	}

	public String getScreenAspect() {
		return screenAspect;
	}

	public void setScreenAspect(String screenAspect) {
		this.screenAspect = screenAspect;
	}

}
