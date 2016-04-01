package com.openfms.model.spi.api;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

import com.openfms.model.exceptions.BaseException;

public interface FmsApi {

	public FmsApiResponse call(String uri, RequestMethod method, Object data, Map<String, Object> requestParams, Map<String, Object> pathVariables) throws BaseException, IOException;

	
}
