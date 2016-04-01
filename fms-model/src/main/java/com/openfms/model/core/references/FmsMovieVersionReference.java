package com.openfms.model.core.references;

import java.util.List;

import com.openfms.model.core.movie.FmsMovieVersion;

public class FmsMovieVersionReference {
	
	private String movieVersionId;
	private String displayName;
	private int status;
	private List<String> audioLanguageIds;
	private List<String> subtitleLanguageIds;
	
	public FmsMovieVersionReference() {
	}
	
	public FmsMovieVersionReference(FmsMovieVersion version) {
		this.status = version.getStatus();
		this.displayName = version.getDisplayName();
		this.setAudioLanguageIds(version.getAudioLanguageIds());
		this.setSubtitleLanguageIds(version.getSubtitleLanguageIds());
		this.movieVersionId = version.getId();
	}

	public String getMovieVersionId() {
		return movieVersionId;
	}

	public void setMovieVersionId(String movieVersionId) {
		this.movieVersionId = movieVersionId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	

}
