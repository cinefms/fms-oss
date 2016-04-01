package com.openfms.model.spi.pdf;


public interface PdfRendererFactory {
	
	public PdfRenderer getPdfRenderer(String basedir);

}
