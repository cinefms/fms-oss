package com.openfms.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ ElementType.TYPE } )
public @interface SearchableField {

	public String value();
	public boolean analyzed() default true;
	
}
