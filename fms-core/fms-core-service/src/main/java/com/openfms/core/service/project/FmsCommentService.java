package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.global.FmsComment;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsCommentService extends GenericService<FmsComment> {

	public List<FmsComment> listComments(String objectId, boolean asc, String order, Integer start, Integer max, String objectType) throws DatabaseException, EntityNotFoundException, AccessDeniedException;

}
