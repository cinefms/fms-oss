package com.openfms.core.service.project.impl.listeners;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.global.FmsMOTD;

@Component
public class MOTDListener extends FmsListenerAdapter<FmsMOTD> {

	
	@Override
	protected boolean beforeSave(String db, FmsMOTD motd)  {
		
		motd.setAuthorName(FmsSessionHolder.getCurrentUser().getName());
		motd.setAuthor(FmsSessionHolder.getCurrentUser().getId());
		motd.setDate(new Date());
		return true;
		
	}

}
