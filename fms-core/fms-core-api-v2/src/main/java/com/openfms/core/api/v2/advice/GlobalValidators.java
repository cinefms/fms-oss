package com.openfms.core.api.v2.advice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import com.openfms.core.api.v2.validators.FmsValidator;

@ControllerAdvice
public class GlobalValidators implements ApplicationContextAware, Validator {

	private static Log log = LogFactory.getLog(GlobalValidators.class);
	
	private ApplicationContext applicationContext;
	private List<FmsValidator> validators;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	public void init() {
		validators = new ArrayList<FmsValidator>(applicationContext.getBeansOfType(FmsValidator.class).values());
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(this);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
		boolean ok = false; 
		try {
			if (log.isDebugEnabled()) {
				if(log.isDebugEnabled()){ log.debug(" ### checking " + validators.size() + " validators ... "); }
			}
			if(target instanceof Collection<?>) {
				if (log.isDebugEnabled()) {
					if(log.isDebugEnabled()){ log.debug(" ### validating elements of collection ... "); }
				}
				for(Object t : ((Collection)target)) {
					if(log.isDebugEnabled()){ log.debug(" ### validating elements of collection ... "+t.getClass()); }
					validate(t, errors);
				}
				return;
			} 
			for(FmsValidator v : validators) {
				if(v.supports(target.getClass())) {
					if (log.isDebugEnabled()) {
						if(log.isDebugEnabled()){ log.debug(" ### validating element ... " + v.getClass()); }
					}
					if (log.isDebugEnabled()) {
						if(log.isDebugEnabled()){ log.debug(" ### using " + v.getClass()); }
					}
					v.validate(target, errors, this);
					ok = true;
				}
			}
		} catch (Exception e) {
			log.error("error validating", e);
			errors.reject("validation_error");
			return;
		}
		if(!ok) {
			errors.reject("no validators matched, but @Valid specified");
		}
	}

}
