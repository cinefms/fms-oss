package com.openfms.model.core.media;

import java.util.ArrayList;
import java.util.List;

public class FmsAudioTrack extends AbstractFmsMediaInfo {
	
	private List<String> languageIds = new ArrayList<String>();

	public List<String> getLanguageIds() {
		return languageIds;
	}

	public void setLanguageIds(List<String> languageIds) {
		if(languageIds==null) {
			this.languageIds.clear();
			return;
		}
		this.languageIds = languageIds;
	}

}
