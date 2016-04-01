package com.openfms.core.api.v2.controllers.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsLanguageService;
import com.openfms.model.core.global.FmsLanguage;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.ParameterOutOfRangeException;
import com.openfms.model.exceptions.RequestedEntityTooLargeException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(GlobalLanguageController.BASE_URI)
public class GlobalLanguageController extends GenericController<FmsLanguage> {

	public static final String BASE_URI = "/global/languages";
	
	@Autowired
	private FmsLanguageService languageService;
	
	@Override
	protected GenericService<FmsLanguage> getService() {
		return languageService;
	}

	@Override
	protected Class<FmsLanguage> getType() {
		return FmsLanguage.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsLanguage> list(
				@RequestParam(required=false) String searchTerm,
				@RequestParam(required=false,value="code") String[] codes,
				@RequestParam(required=false,defaultValue="0") Integer start, 
				@RequestParam(required=false,defaultValue="25") int max
			) throws RequestedEntityTooLargeException, ParameterOutOfRangeException, AccessDeniedException, DatabaseException {
		if(codes==null || codes.length==0) {
			DBStoreQuery q = BasicQuery.createQuery();
			if(searchTerm!=null) {
				q = q.in("codes", searchTerm.toLowerCase());
			}
			q = q.start(start);
			q = q.max(max);
			List<FmsLanguage> out = languageService.list(q);
			if(out.size()>0) {
				return out;
			}
		}
		return languageService.listLanguages(searchTerm,codes, start, max);
	}
	
	@RequestMapping(value="",method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public void initLanguages() throws AccessDeniedException, DatabaseException, VersioningException {
		languageService.initLanguages();
	}
	
	
}
