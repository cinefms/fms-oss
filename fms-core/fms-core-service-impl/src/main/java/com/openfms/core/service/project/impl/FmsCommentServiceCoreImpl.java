package com.openfms.core.service.project.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.project.FmsCommentService;
import com.openfms.model.core.global.FmsComment;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Service
public class FmsCommentServiceCoreImpl extends GenericProjectServiceImpl<FmsComment> implements FmsCommentService {

	@Override
	public List<FmsComment> listComments(String objectId, boolean asc, String order, Integer start, Integer max, String objectType) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if (objectId != null) {
			q = q.eq("objectId", objectId);
		}
		if (objectType != null) {
			q = q.eq("objectType", objectType);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max == null ? -1 : max);
		return list(q);
	}

}
