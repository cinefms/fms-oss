package com.openfms.model.core.global;

import java.util.List;

public class FmsUpdateResult {

	private Class service;
	private int total, updated, errors;
	private List<String> exceptions;
	private long totalTime;
	private long maxTime;
	private long avg;

	public FmsUpdateResult(Class service, int total, int updated, int errors, List<String> exceptions) {
		super();
		this.service = service;
		this.total = total;
		this.updated = updated;
		this.errors = errors;
		this.exceptions = exceptions;
	}

	public Class getService() {
		return service;
	}

	public void setService(Class service) {
		this.service = service;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	public int getErrors() {
		return errors;
	}

	public void setErrors(int errors) {
		this.errors = errors;
	}

	public List<String> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<String> exceptions) {
		this.exceptions = exceptions;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public long getAvg() {
		return avg;
	}

	public void setAvg(long avg) {
		this.avg = avg;
	}

}
