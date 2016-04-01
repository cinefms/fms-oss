package com.openfms.core.service.admin;

import java.util.List;

import com.openfms.model.core.global.FmsUpdateResult;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;


public interface ProjectService {

	public FmsProject getProjectForHost(String host) throws DatabaseException;

	public List<FmsProject> getProjects(String searchTerm, Integer start, Integer max) throws DatabaseException, AccessDeniedException;

	public FmsProject getProject(String projectId) throws DatabaseException, AccessDeniedException, EntityNotFoundException;

	public FmsProject saveProject(FmsProject project) throws DatabaseException, AccessDeniedException, VersioningException;

	public void deleteProject(String projectId) throws DatabaseException, AccessDeniedException;

	public List<FmsUpdateResult> updateAll() throws AccessDeniedException;

}
