package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.global.FmsLanguage;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.VersioningException;

public interface FmsLanguageService extends GenericService<FmsLanguage> {

	public List<FmsLanguage> listLanguages(String searchTerm, String[] codes, Integer start, Integer max) throws AccessDeniedException, DatabaseException;

	public void initLanguages() throws AccessDeniedException, VersioningException;

}
