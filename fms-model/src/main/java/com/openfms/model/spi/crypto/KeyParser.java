package com.openfms.model.spi.crypto;

import java.io.IOException;
import java.util.List;

import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.crypto.FmsKeyParseResult;


public interface KeyParser {

	public boolean isValidKDM(byte[] data);
	
	public FmsKey parseKdm(String source, String filename, String movieId, byte[] data) throws IOException;

	public List<FmsKeyParseResult> parseKdms(String source, String filename, String movieId, byte[] data) throws IOException;
	

}
