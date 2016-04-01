package com.openfms.model.spi.config;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;

public interface FmsHome {

	public File findConfigDir(String instance, String postfix) throws IOException;
	
	public Attributes getApplicationAttributes();

}