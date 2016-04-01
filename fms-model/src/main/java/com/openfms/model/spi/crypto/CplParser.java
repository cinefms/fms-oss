package com.openfms.model.spi.crypto;

import java.io.IOException;

import com.openfms.model.core.movie.FmsMediaClip;


public interface CplParser {

	public boolean isValidCPL(byte[] data);
	
	public FmsMediaClip parseCpl(byte[] data) throws IOException;

}
