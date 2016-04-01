package com.openfms.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.openfms.model.FmsObjectChangeNotifier.OPERATION;

@Target(value={ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FmsNotify {
	
	public static final String CURRENT_ROLES = "_CURRENT_ROLE";

	public OPERATION[] operations();
	public String[] runWithRoles() default CURRENT_ROLES;
	//public boolean runAsync() default false;
	
}
