package com.openfms.core.api.v2.advice;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

import com.openfms.core.api.v2.advice.editors.CustomBooleanEditor;
import com.openfms.core.api.v2.advice.editors.CustomDateEditor;
import com.openfms.core.api.v2.advice.editors.CustomDateRangeEditor;
import com.openfms.core.api.v2.utils.DateRange;

@ControllerAdvice
public class GlobalDataBinders {

	@InitBinder
	public void registerCustomEditors(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor());
		binder.registerCustomEditor(Date.class, new CustomDateEditor());
		binder.registerCustomEditor(DateRange.class, new CustomDateRangeEditor());
	}

}
