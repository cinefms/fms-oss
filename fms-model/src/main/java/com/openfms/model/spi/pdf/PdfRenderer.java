package com.openfms.model.spi.pdf;

import java.util.Map;

import com.openfms.model.exceptions.RenderingException;

public interface PdfRenderer {

	public abstract byte[] renderPdf(String template, Map<String, Object> model) throws RenderingException;

}
