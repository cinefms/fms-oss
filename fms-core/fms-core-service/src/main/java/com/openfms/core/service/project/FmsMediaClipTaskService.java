package com.openfms.core.service.project;

import java.util.Date;
import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.movie.FmsMediaClipTask;
import com.openfms.model.core.movie.FmsMediaClipTaskProgress;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

public interface FmsMediaClipTaskService extends GenericService<FmsMediaClipTask> {

	public List<FmsMediaClipTask> listMediaClipTasks(String mediaClipId, String userId, String projectUserId, String[] types, Integer[] status, Date usedAfter, String order, boolean asc, Integer start, Integer max) throws DatabaseException, EntityNotFoundException, AccessDeniedException;

	public FmsMediaClipTaskProgress addProgress(FmsMediaClipTaskProgress progress) throws AccessDeniedException, DatabaseException, EntityNotFoundException, VersioningException;

	public List<FmsMediaClipTaskProgress> getProgress(String mediaClipTaskId) throws AccessDeniedException, DatabaseException;


}
