package com.openfms.core.service.project;

import java.io.IOException;
import java.util.Map;

import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;


public interface FmsMailService {
	
	public void sendMail(String mail, Map<String,Object> tokens, String... recipients) throws DatabaseException, EntityNotFoundException, IOException;

}
