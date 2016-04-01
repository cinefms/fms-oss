package com.openfms.model.spi.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfms.utils.common.IOUtils;

public class FmsApiResponse {

	private static Log log = LogFactory.getLog(FmsApiResponse.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private byte[] contents;
	
	private StatusLine httpStatus;
	private RequestMethod method;
	private String url; 
	
	public FmsApiResponse(RequestMethod method, String url, HttpResponse response) throws IllegalStateException, IOException {
		if(response.getEntity()!=null && response.getEntity().getContent()!=null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(response.getEntity().getContent(), baos);
			this.contents = baos.toByteArray();
		}
		this.httpStatus = response.getStatusLine();
		this.method = method;
		this.url = url;
	}
	
	public StatusLine getStatusLine() {
		return httpStatus;
	}

	public String getContentsAsString() throws UnsupportedEncodingException {
		return new String(contents,"utf-8");
	}
	
	public byte[] getContents() throws UnsupportedEncodingException {
		return contents;
	}
	
	public <T> T getObject(Class<T> as) throws IOException {
		try {
			return (T)objectMapper.readValue(contents, as);
		} catch (JsonProcessingException e) {
			log.error("text was: "+getContentsAsString(),e);
			throw new IOException(method+" - "+url, e);
		}
	}

	public <T> List<T> getObjects(Class<T> as) throws IOException {
		try {
			@SuppressWarnings("unchecked")
			List<T> out = (List<T>)objectMapper.readValue(contents, objectMapper.getTypeFactory().constructCollectionType(List.class, as));
			return out;
		} catch (JsonProcessingException e) {
			log.error("text was: "+getContentsAsString(),e);
			throw new IOException(method+" - "+url, e);
		}
	}
	
}
