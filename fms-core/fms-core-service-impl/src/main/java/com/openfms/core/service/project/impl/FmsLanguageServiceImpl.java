package com.openfms.core.service.project.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.impl.listeners.FmsListener;
import com.openfms.core.service.project.FmsLanguageService;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.global.FmsLanguage;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.NotAuthorizedException;
import com.openfms.model.exceptions.VersioningException;
import com.openfms.utils.common.text.i18n.Language;
import com.openfms.utils.common.text.i18n.LanguageReader;

@Service
public class FmsLanguageServiceImpl extends GenericProjectServiceImpl<FmsLanguage> implements FmsLanguageService, FmsListener {
	
	@Autowired
	private AuthzService authzService;
	
	
	@PostConstruct
	public void init() {
		
	}
	
	@Override
	public List<FmsLanguage> listLanguages(String searchTerm, String[] codes, Integer start, Integer max) throws AccessDeniedException, DatabaseException {

		List<FmsLanguage> out = new ArrayList<FmsLanguage>();
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
	public void initLanguages() throws AccessDeniedException, VersioningException {
		if(!authzService.allowAccess(FmsLanguage.class, AccessType.ADMIN)) {
			throw new NotAuthorizedException();
		}
		for(Language l : LanguageReader.getLanguages()) {
			FmsLanguage fl = new FmsLanguage();
			fl.setName(l.getName());
			fl.setCode(l.getCode());
			fl.setCodes(l.getCodes());
			try {
				save(fl);
			} catch (AccessDeniedException | DatabaseException e) {
			}
		}
	}

	@Override
	public void created(String db, FmsObject o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleted(String db, FmsObject o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeDelete(String db, FmsObject o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updated(String db, FmsObject o, FmsObject n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean beforeSave(String db, FmsObject o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supports(Class<? extends FmsObject> clazz) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
