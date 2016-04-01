package com.openfms.utils.pdf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.openfms.model.spi.pdf.PdfRenderer;
import com.openfms.model.spi.pdf.PdfRendererFactory;

@Component
public class PdfRendererFactoryImpl implements PdfRendererFactory {

	private static Log log = LogFactory.getLog(PdfRendererFactoryImpl.class);
	
	@Override
	public PdfRenderer getPdfRenderer(String basedir) {
		log.info("instantiating PDF renderer at: "+basedir);
		PdfRenderer out = new PdfRendererImpl(basedir);
		return out;
	}

}
