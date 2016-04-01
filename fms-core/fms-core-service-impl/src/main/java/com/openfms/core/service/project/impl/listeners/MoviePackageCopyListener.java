package com.openfms.core.service.project.impl.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsFileCopyService;
import com.openfms.core.service.project.FmsFileService;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMoviePackageCopyService;
import com.openfms.core.service.project.FmsMoviePackageService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.model.core.storage.FmsFileCopy;
import com.openfms.model.core.storage.FmsMoviePackageCopy;


@Component
public class MoviePackageCopyListener extends FmsListenerAdapter<FmsMoviePackageCopy> {
	
	@Autowired
	private FmsMovieVersionService movieVersionService;
	
	@Autowired
	private FmsEventService eventService;
	
	@Autowired
	private FmsMediaClipService mediaClipService;
	
	@Autowired
	private FmsMoviePackageService moviePackageService;
	
	@Autowired
	private FmsMoviePackageCopyService moviePackageCopyService;
	
	@Autowired
	private FmsFileService fileService;
	
	@Autowired
	private FmsFileCopyService fileCopyService;
	
	protected MoviePackageCopyListener() {
	}
	
	@Override
	protected void beforeDelete(String db, FmsMoviePackageCopy object) {
		try {
			
			for(FmsFileCopy fc : fileCopyService.list(BasicQuery.createQuery().eq("packageCopyId", object.getId()))) {
				fileCopyService.delete(fc.getId());
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException("error deleting or updating dependent data ... ", e);
		}
		
	}


}
