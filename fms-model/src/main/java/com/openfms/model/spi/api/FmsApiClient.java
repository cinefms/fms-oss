package com.openfms.model.spi.api;

import java.io.IOException;
import java.util.Map;

import org.bouncycastle.crypto.tls.ContentType;
import org.springframework.web.bind.annotation.RequestMethod;

import com.openfms.model.core.config.FmsCredentials;
import com.openfms.model.exceptions.BaseException;

public interface FmsApiClient {

	public abstract FmsApiResponse call(String uri, RequestMethod method, Object data, Map<String, Object> requestParams, Map<String, Object> pathVariables) throws BaseException, IOException;

	public abstract boolean authenticate(FmsCredentials credentials);

	public abstract void setSessionId(String sessionId);

	public abstract String getSessionId();

	public abstract FmsApiResponse put(String uri, Object data) throws BaseException, IOException;

	public abstract FmsApiResponse post(String uri, String name, String filename, ContentType contentType, Object data, Map<String, String> requestParams) throws BaseException, IOException;

	public abstract FmsApiResponse post(String uri, Object data) throws BaseException, IOException;

}
