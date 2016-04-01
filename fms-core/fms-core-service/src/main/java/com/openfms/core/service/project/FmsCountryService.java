package com.openfms.core.service.project;

import java.util.List;

import com.openfms.core.service.GenericService;
import com.openfms.model.core.global.FmsCountry;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.VersioningException;

public interface FmsCountryService extends GenericService<FmsCountry> {

	public List<FmsCountry> listCountries(String searchTerm, String[] code, Integer start, Integer max) throws AccessDeniedException, DatabaseException;

	public void initCountries() throws AccessDeniedException, VersioningException;

}
