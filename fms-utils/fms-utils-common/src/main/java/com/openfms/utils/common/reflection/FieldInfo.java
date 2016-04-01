package com.openfms.utils.common.reflection;

public class FieldInfo implements Comparable<FieldInfo> {

	private String name;
	private String path;
	private boolean collection;
	private Class<?> type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isCollection() {
		return collection;
	}

	public void setCollection(boolean collection) {
		this.collection = collection;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	@Override
	public int compareTo(FieldInfo o) {
		return (getName()+"").compareTo(o.getName()+"");
	}

}
