package com.openfms.core.api.v2.advice.editors;

import java.beans.PropertyEditorSupport;

public class CustomBooleanEditor extends PropertyEditorSupport {

	public static final String VALUE_TRUE = "true";
	public static final String VALUE_FALSE = "false";
	public static final String VALUE_ALL = "all";

	public CustomBooleanEditor() {
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		text = (text != null ? text.trim() : "all");
		if (text.equalsIgnoreCase(VALUE_TRUE)) {
			setValue(Boolean.TRUE);
		} else if (text.equalsIgnoreCase(VALUE_FALSE)) {
			setValue(Boolean.FALSE);
		} else if (text.equalsIgnoreCase(VALUE_ALL)) {
			setValue(null);
		} else {
			throw new IllegalArgumentException("Invalid boolean value [" + text + "]");
		}
	}
}