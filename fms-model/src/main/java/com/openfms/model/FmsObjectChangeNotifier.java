package com.openfms.model;

// marker interface 
public interface FmsObjectChangeNotifier {
	
	public enum OPERATION {
		PREPARE,
		DELETE, DELETED,
		UPDATE, UPDATED;
	}
	
	public <T> void prepareObject(T o);
	public <T> void objectDelete(T o);
	public <T> void objectDeleted(T o);
	public <T> void objectUpdate(T newVersion, T oldVersion);
	public <T> void objectUpdated(T newVersion, T oldVersion);
	
}
