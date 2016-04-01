package com.openfms.core.service.project;

import java.util.List;

import com.openfms.model.core.auth.FmsHistory;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsHistoryService {

	public List<FmsHistory> listHistory(String modifiedObjectId, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException;

}
