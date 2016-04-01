package com.openfms.model.core.playback;

import java.util.ArrayList;
import java.util.List;

import com.openfms.model.core.references.FmsMovieVersionReference;

public class FmsEventAutoAssignLogItem {
	
	private String movieId;
	private String movieName;
	
	private boolean modified;

	private List<FmsMovieVersionReference> candidates = new ArrayList<>();
	
	public String getMovieId() {
		return movieId;
	}
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public boolean isModified() {
		return modified;
	}
	public void setModified(boolean modified) {
		this.modified = modified;
	}
	public List<FmsMovieVersionReference> getCandidates() {
		return candidates;
	}
	public void setCandidates(List<FmsMovieVersionReference> candidates) {
		this.candidates = candidates;
	}

}
