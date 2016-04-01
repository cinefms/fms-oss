package com.openfms.core.api.v2.advice.editors;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import org.springframework.util.StringUtils;

public class CustomDateEditor extends PropertyEditorSupport {

	private boolean allowEmpty = true;

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			setValue(null);
		} else {
			if (text.compareToIgnoreCase("now") == 0) {
				setValue(new Date());
			} else {
				try {
					Long l = Long.parseLong(text);
					setValue(new Date(l.longValue()));
				} catch (NumberFormatException ex) {
					throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
				}
			}
		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? value.getTime() : null) + "";
	}
}