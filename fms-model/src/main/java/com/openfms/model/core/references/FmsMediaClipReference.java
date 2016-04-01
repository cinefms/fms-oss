package com.openfms.model.core.references;

import java.util.List;

import com.openfms.model.core.movie.FmsMediaClip;

public class FmsMediaClipReference {

	private String mediaClipId;
	private String mediaClipName;
	private String mediaClipType;
	private String mediaClipExternalId;
	private List<String> moviePackageIds;
	/**
	 * 
	 */
	private List<String> fileIds;
	private List<String> tags;
	private List<String> keyIds;
	private List<String> audioLanguageIds;
	private List<String> subtitleLanguageIds;
	private boolean encrypted;
	private boolean disabled;
	private int length;
	private int status;
	
	public FmsMediaClipReference() {
	}

	public FmsMediaClipReference(FmsMediaClip mc) {
		this.mediaClipId = mc.getId();
		this.mediaClipName = mc.getName();
		this.mediaClipType = mc.getType();
		this.tags = mc.getTags();
		this.keyIds = mc.getKeyIds();
		this.encrypted = mc.isEncrypted();
		this.disabled=mc.isDisabled();
		this.fileIds = mc.getFileIds();
		this.moviePackageIds = mc.getMoviePackageIds();
		this.length = mc.getLength();
		this.audioLanguageIds = mc.getAudioLanguageIds();
		this.subtitleLanguageIds = mc.getSubtitleLanguageIds();
		this.setMediaClipExternalId(mc.getExternalId());
		this.setStatus(mc.getStatus());
	}
	
	public String getMediaClipId() {
		return mediaClipId;
	}

	public void setMediaClipId(String mediaClipId) {
		this.mediaClipId = mediaClipId;
	}

	public String getMediaClipName() {
		return mediaClipName;
	}

	public void setMediaClipName(String mediaClipName) {
		this.mediaClipName = mediaClipName;
	}

	public String getMediaClipType() {
		return mediaClipType;
	}

	public void setMediaClipType(String mediaClipType) {
		this.mediaClipType = mediaClipType;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getMoviePackageIds() {
		return moviePackageIds;
	}

	public void setMoviePackageIds(List<String> moviePackageIds) {
		this.moviePackageIds = moviePackageIds;
	}

	public List<String> getFileIds() {
		return fileIds;
	}

	public void setFileIds(List<String> fileIds) {
		this.fileIds = fileIds;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMediaClipExternalId() {
		return mediaClipExternalId;
	}

	public void setMediaClipExternalId(String mediaClipExternalId) {
		this.mediaClipExternalId = mediaClipExternalId;
	}

	public List<String> getKeyIds() {
		return keyIds;
	}

	public void setKeyIds(List<String> keyIds) {
		this.keyIds = keyIds;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
