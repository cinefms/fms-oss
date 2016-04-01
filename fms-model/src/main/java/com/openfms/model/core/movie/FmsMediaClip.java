package com.openfms.model.core.movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.Status;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;
import com.openfms.model.core.media.FmsMovieReel;
import com.openfms.model.core.references.FmsEventReference;

@Indexes(
	{
		@Index(fields={"type"},name="ep_type",unique=false),
		@Index(fields={"contentTitleText"},name="ep_contentTitleText",unique=false),
		@Index(fields={"firstEventDate"},name="ep_firstEventDate",unique=false),
		@Index(fields={"lastEventDate"},name="ep_lastEventDate",unique=false),
		@Index(fields={"externalId","movieId"},name="mediaClipUniqueExternalId",unique=false),
		@Index(fields={"md5"},name="idxHash",unique=false),
		@Index(fields={"moviePackageIds"},name="idxMoviePackageIds",unique=false),
		@Index(fields={"name"},name="fmp_name", unique=false),
		@Index(fields={"disabled"},name="fmp_disabled ", unique=false)
	}
)
@FmsAccessControlled(FmsAccessControlled.GROUP.MEDIA)
public class FmsMediaClip extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	private String type;
	private String externalId;
	private String movieId;
	private String movieExternalId;
	private String movieName;
	private String movieCategory;
	private String creator;
	private String contentTitleText;
	private String annotationText;
	private String mediaAspect;
	private String screenAspect;
	private String screeningRemarks;
	private String audioFormat;
	private String fps;
	private String md5;
	private int length = -1;
	private int status = Status.PENDING.value();
	private boolean stereoscopic;
	private boolean interlaced;
	private boolean disabled;
	private boolean encrypted;
	private boolean ignoreLanguage;
	

	private Date firstEventDate;
	private Date lastEventDate;

	private List<String> taskIds = new ArrayList<String>();
	private int openTasks = 0;
	
	
	private List<MediaClipAttachmentLink> attachments = new ArrayList<MediaClipAttachmentLink>();
	
	private List<String> moviePackageIds = new ArrayList<String>();
	
	private List<FmsMovieReel> reels = new ArrayList<FmsMovieReel>();
	private List<String> fileIds = new ArrayList<String>();
	private List<String> keyIds = new ArrayList<String>();
	private List<String> audioLanguageIds = new ArrayList<String>();
	private List<String> subtitleLanguageIds = new ArrayList<String>();
	
	private Map<String, Object> data = new HashMap<String, Object>(); 
	private String dataId; 

	private Date nextEventDate;
	private String nextEventId;
	
	private List<String> tags = new ArrayList<String>();
	private List<FmsEventReference> events = new ArrayList<FmsEventReference>();

	public FmsMediaClip() {
		super(null);	
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<FmsMovieReel> getReels() {
		return reels;
	}

	public void setReels(List<FmsMovieReel> reels) {
		if(reels==null) {
			this.reels.clear();
			return;
		}
		this.reels = reels;
	}

	public List<String> getMoviePackageIds() {
		return moviePackageIds;
	}

	public void setMoviePackageIds(List<String> moviePackageIds) {
		if(moviePackageIds==null) {
			this.moviePackageIds.clear();
			return;
		}
		this.moviePackageIds = moviePackageIds;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public List<String> getKeyIds() {
		return keyIds;
	}

	public void setKeyIds(List<String> keyIds) {
		this.keyIds = keyIds;
	}

	public List<String> getAudioLanguageIds() {
		return audioLanguageIds!=null?audioLanguageIds:new ArrayList<String>();
	}

	public void setAudioLanguageIds(List<String> audioLanguageIds) {
		if(audioLanguageIds==null) {
			this.audioLanguageIds.clear();
			return;
		}
		this.audioLanguageIds = audioLanguageIds;
	}

	public List<String> getSubtitleLanguageIds() {
		return subtitleLanguageIds!=null?subtitleLanguageIds:new ArrayList<String>();
	}

	public void setSubtitleLanguageIds(List<String> subtitleLanguageIds) {
		if(subtitleLanguageIds==null) {
			this.subtitleLanguageIds.clear();
			return;
		}
		this.subtitleLanguageIds = subtitleLanguageIds;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getContentTitleText() {
		return contentTitleText;
	}

	public void setContentTitleText(String contentTitleText) {
		this.contentTitleText = contentTitleText;
	}

	public String getAnnotationText() {
		return annotationText;
	}

	public void setAnnotationText(String annotationText) {
		this.annotationText = annotationText;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
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

	public String getScreeningRemarks() {
		return screeningRemarks;
	}

	public void setScreeningRemarks(String screeningRemarks) {
		this.screeningRemarks = screeningRemarks;
	}

	public String getFps() {
		return fps;
	}

	public void setFps(String fps) {
		this.fps = fps;
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

	public boolean isStereoscopic() {
		return stereoscopic;
	}

	public void setStereoscopic(boolean stereoscopic) {
		this.stereoscopic = stereoscopic;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isInterlaced() {
		return interlaced;
	}

	public void setInterlaced(boolean interlaced) {
		this.interlaced = interlaced;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
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
		this.tags.clear();
		if(tags==null) {
			return;
		}
		this.tags.addAll(tags);
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

	@Override
	public String getSearchable() {
		return concat(getExternalId(),getAnnotationText(),getContentTitleText(),getName(),getMovieExternalId());
	}

	public List<MediaClipAttachmentLink> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<MediaClipAttachmentLink> attachments) {
		this.attachments = attachments;
	}

	public String getAudioFormat() {
		return audioFormat;
	}

	public void setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
	}

	public List<String> getFileIds() {
		return fileIds;
	}

	public void setFileIds(List<String> fileIds) {
		this.fileIds = fileIds;
	}

	public String getMovieExternalId() {
		return movieExternalId;
	}

	public void setMovieExternalId(String movieExternalId) {
		this.movieExternalId = movieExternalId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public List<String> getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(List<String> taskIds) {
		this.taskIds = taskIds;
	}

	public int getOpenTasks() {
		return openTasks;
	}

	public void setOpenTasks(int openTasks) {
		this.openTasks = openTasks;
	}

	public boolean isIgnoreLanguage() {
		return ignoreLanguage;
	}

	public void setIgnoreLanguage(boolean ignoreLanguage) {
		this.ignoreLanguage = ignoreLanguage;
	}

	public String getMovieCategory() {
		return movieCategory;
	}

	public void setMovieCategory(String movieCategory) {
		this.movieCategory = movieCategory;
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

	
}
