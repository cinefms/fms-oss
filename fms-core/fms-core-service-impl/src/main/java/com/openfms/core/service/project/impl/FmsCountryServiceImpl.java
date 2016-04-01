package com.openfms.core.service.project.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.project.FmsCountryService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.global.FmsCountry;
import com.openfms.model.core.global.FmsLanguage;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.NotAuthorizedException;
import com.openfms.model.exceptions.VersioningException;
import com.openfms.utils.common.text.i18n.Country;
import com.openfms.utils.common.text.i18n.CountryReader;

@Service
public class FmsCountryServiceImpl extends GenericProjectServiceImpl<FmsCountry> implements FmsCountryService {

	@Autowired
	private AuthzService authzService;

	@Override
	public List<FmsCountry> listCountries(String searchTerm, String[] codes, Integer start, Integer max) throws AccessDeniedException, DatabaseException {

		List<FmsCountry> out = new ArrayList<FmsCountry>();
		DBStoreQuery q = BasicQuery.createQuery();
		
		if(codes!=null && codes.length>0) {
			List<DBStoreQuery> idq = new ArrayList<DBStoreQuery>();
			for(String code : codes) {
				idq.add(BasicQuery.createQuery().eq("code", code));
			}
			q = q.or(idq);
		}
		
		if(searchTerm!=null && searchTerm.length()>0) {
			List<DBStoreQuery> idq = new ArrayList<DBStoreQuery>();
			idq.add(BasicQuery.createQuery().contains("name", searchTerm));
			idq.add(BasicQuery.createQuery().contains("codes", searchTerm));
			q = q.or(idq);
		}
		
		out = list(q);
		start = Math.min(start, out.size());
		max = Math.min(max + start, out.size()) - start;
		out = out.subList(start, start + max);
		return out;
	}
	
	@Override
	public void initCountries() throws AccessDeniedException, VersioningException {
		if(!authzService.allowAccess(FmsLanguage.class, AccessType.ADMIN)) {
			throw new NotAuthorizedException();
		}
		for(Country c : CountryReader.getCountries()) {
			FmsCountry fl = new FmsCountry();
			fl.setName(c.getName());
			fl.setCode(c.getCode());
			fl.setCodes(c.getCodes());
			try {
				save(fl);
			} catch (AccessDeniedException | DatabaseException e) {
			}
		}
	}
	

}
