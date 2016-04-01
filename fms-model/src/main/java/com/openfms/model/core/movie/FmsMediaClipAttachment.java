package com.openfms.model.core.movie;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.MEDIA)
public class FmsMediaClipAttachment extends AbstractFmsObject {

	private static final long serialVersionUID = -4918659802259173547L;
	
	private String mediaClipId;
	private String type;
	private String thumbnail;
	private String url;

	public FmsMediaClipAttachment() {
		super(null);
	}

	public String getMediaClipId() {
		return mediaClipId;
	}

	public void setMediaClipId(String mediaClipId) {
		this.mediaClipId = mediaClipId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

}
