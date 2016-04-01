package com.openfms.model.core.movie;


public class FmsMediaClipTaskTypeNext {

	private boolean def;
	private String nextTaskType;
	private String addTag;

	public FmsMediaClipTaskTypeNext() {
	}
	
	public FmsMediaClipTaskTypeNext(String nextTaskType) {
		super();
		this.nextTaskType = nextTaskType;
	}
	
	
	public boolean isDef() {
		return def;
	}

	public void setDef(boolean def) {
		this.def = def;
	}

	public String getNextTaskType() {
		return nextTaskType;
	}

	public void setNextTaskType(String nextTaskType) {
		this.nextTaskType = nextTaskType;
	}

	public String getAddTag() {
		return addTag;
	}

	public void setAddTag(String addTag) {
		this.addTag = addTag;
	}

}
