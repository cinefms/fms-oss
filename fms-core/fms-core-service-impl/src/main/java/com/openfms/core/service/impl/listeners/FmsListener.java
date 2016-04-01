package com.openfms.core.service.impl.listeners;

import com.openfms.model.core.FmsObject;

public interface FmsListener {

	public abstract void created(String db, FmsObject o);

	public abstract void deleted(String db, FmsObject o);

	public abstract void beforeDelete(String db, FmsObject o);

	public abstract void updated(String db, FmsObject o, FmsObject n);

	public abstract boolean beforeSave(String db, FmsObject o);

	public abstract boolean supports(Class<? extends FmsObject> clazz);

}
