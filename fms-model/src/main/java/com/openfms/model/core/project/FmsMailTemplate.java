package com.openfms.model.core.project;

import java.util.Map;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;

@FmsAccessControlled(FmsAccessControlled.GROUP.PROJECT)
public class FmsMailTemplate extends AbstractFmsObject {

	private static final long serialVersionUID = 7356539347606835474L;
	private String sender;
	private String subject;
	private String body;
	private Map<String,Object> examples;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Map<String,Object> getExamples() {
		return examples;
	}

	public void setExamples(Map<String,Object> examples) {
		this.examples = examples;
	}

}
