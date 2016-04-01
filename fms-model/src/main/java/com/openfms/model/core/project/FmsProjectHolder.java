package com.openfms.model.core.project;



public class FmsProjectHolder {

	private static ThreadLocal<FmsProject> holder = new ThreadLocal<FmsProject>();
	
	public static FmsProject get() {
		return holder.get();
	}

	public static void set(FmsProject session) {
		if(holder.get()!=null) {
			System.err.println("replacing "+holder.get()+" with "+session);
		}
		holder.set(session);
	}

	public static void clear() {
		holder.remove();
	}
	
}
