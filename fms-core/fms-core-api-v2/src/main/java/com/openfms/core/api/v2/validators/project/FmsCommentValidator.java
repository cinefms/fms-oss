package com.openfms.core.api.v2.validators.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cinefms.dbstore.api.DataStore;
import com.openfms.core.api.v2.validators.AbstractValidator;
import com.openfms.core.api.v2.validators.ErrorCodes;
import com.openfms.core.service.admin.ProjectService;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.global.FmsComment;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectHolder;

@Component
public class FmsCommentValidator extends AbstractValidator<FmsComment> {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private DataStore dataStore;

	public FmsCommentValidator() {
		super(FmsComment.class);
		setNonEmpty("text", "objectId", "objectType");
	}
	
	@Override
	public void validateInternal(FmsComment target, Errors errors, Validator v) {
		try {
			FmsProject p = FmsProjectHolder.get();
			if(p==null) {
				throw new RuntimeException("no project");
			}
			Class<FmsObject> c = null;
			try {
				c = (Class<FmsObject>)Class.forName(target.getObjectType());
			} catch (Exception e) {
				throw new RuntimeException("invalid type",e);
			}
			Object o = dataStore.getObject(p.getId(), c, target.getObjectId());
			if(o==null) {
				throw new RuntimeException("invalid id");
			}
		} catch (Exception e) {
			errors.reject(ErrorCodes.INVALID_VALUE, e.getMessage());
		}
	}
	
}
