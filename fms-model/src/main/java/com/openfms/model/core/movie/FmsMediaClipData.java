package com.openfms.model.core.movie;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@Indexes({ @Index(fields = { "mediaClipId" }, name = "mcd_mcid", unique = true), })
@FmsAccessControlled(FmsAccessControlled.GROUP.MEDIA)
public class FmsMediaClipData extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	private String mediaClipId;
	private byte[] data;

	public String getMediaClipId() {
		return mediaClipId;
	}

	public void setMediaClipId(String mediaClipId) {
		this.mediaClipId = mediaClipId;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
