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

import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsCountryService;
import com.openfms.model.core.global.FmsCountry;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(GlobalCountryController.BASE_URI)
public class GlobalCountryController extends GenericController<FmsCountry> {

	public static final String BASE_URI = "/global/countries";

	@Autowired
	private FmsCountryService countryService;
	
	@Override
	protected GenericService<FmsCountry> getService() {
		return countryService;
	}

	@Override
	protected Class<FmsCountry> getType() {
		return FmsCountry.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsCountry> getCountriesBySearch(
				@RequestParam(required=false) String searchTerm, 
				@RequestParam(required=false,value="code") String[] codes, 
				@RequestParam(required=false,defaultValue="0") Integer start, 
				@RequestParam(required=false,defaultValue="25") int max
			) throws AccessDeniedException, DatabaseException {
		return countryService.listCountries(searchTerm, codes, start, max);
	}

	@RequestMapping(value="",method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public void initCountries() throws AccessDeniedException, DatabaseException, VersioningException {
		countryService.initCountries();
	}
	

	
}
