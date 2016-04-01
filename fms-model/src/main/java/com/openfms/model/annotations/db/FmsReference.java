package com.openfms.model.annotations.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.openfms.model.core.FmsObject;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FmsReference {

	public Class<FmsObject> value();
	
}
