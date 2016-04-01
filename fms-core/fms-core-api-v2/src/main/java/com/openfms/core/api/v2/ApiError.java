package com.openfms.core.api.v2;

import com.openfms.model.exceptions.FmsException;


public class ApiError {

	private String errorCode;
	private String errorClass;
	private String errorMessage;
	private String clazz;
	private String method;
	private int line;
	private ApiError causedBy;

	public ApiError(Throwable error) {
		if(error!=null) {
			errorClass = error.getClass().getCanonicalName();
			errorMessage = error.getLocalizedMessage();
			if(error instanceof FmsException) {
				errorCode = ((FmsException)error).getKey();
			}
			StackTraceElement[] ste = error.getStackTrace();
			if(ste!=null && ste.length>0) {
				clazz = ste[0].getClassName();
				method = ste[0].getMethodName();
				line = ste[0].getLineNumber();
			}
			if(error.getCause()!=null) {
				this.causedBy = new ApiError(error.getCause());
			}
		}
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorClass() {
		return errorClass;
	}

	public void setErrorClass(String errorClass) {
		this.errorClass = errorClass;
	}

	public ApiError getCausedBy() {
		return causedBy;
	}

	public void setCausedBy(ApiError causedBy) {
		this.causedBy = causedBy;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

}
