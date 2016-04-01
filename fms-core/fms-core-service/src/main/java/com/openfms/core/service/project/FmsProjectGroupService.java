package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.auth.FmsGroup;
import com.openfms.model.exceptions.EntityNotFoundException;

public interface FmsProjectGroupService extends GenericService<FmsGroup> {

	public List<String> getControlledEntities();

	public List<String> getControlledEntityGroups(String searchTerm);

	public List<String> getAccessTypes(String searchTerm);

	public String getAccessType(String id) throws EntityNotFoundException;

	public String getEntityGroup(String id) throws EntityNotFoundException;


}
