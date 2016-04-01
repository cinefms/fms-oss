package com.openfms.model.utils;

import com.cinefms.dbstore.api.DBStoreEntity;

public class ListenerTrackerHolder {

	
	private static ThreadLocal<ListenerTracker> tracker = new ThreadLocal<ListenerTracker>();
	
	
	protected static ListenerTracker get() {
		ListenerTracker lt = tracker.get();
		if(lt == null) {
			lt = new ListenerTracker();
			tracker.set(lt);
		}
		return lt;
	}
	
	public static boolean add(DBStoreEntity o) {
		return get().add(o);
	}
	
	public static void remove(DBStoreEntity o) {
		get().remove(o);
	}

	public static void clear() {
		tracker.remove();
	}
	
	

}
