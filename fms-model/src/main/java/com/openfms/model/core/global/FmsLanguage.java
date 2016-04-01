package com.openfms.model.core.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;


@FmsAccessControlled(FmsAccessControlled.GROUP.TYPES)
public class FmsLanguage extends AbstractFmsObject {

	private static final long serialVersionUID = -8195522109600526685L;

	private String code;
	private List<String> codes = new ArrayList<String>();

	public String getId() {
		return code==null?super.getId():code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getCodes() {
		return Collections.unmodifiableList(codes);
	}
	
	public void addCode(String code) {
		if(!getCodes().contains(code)) {
			getCodes().add(code);
		}
	}
	
	public void removeCode(String code) {
		getCodes().remove(code);
	}

	public void setCodes(List<String> codes) {
		if(codes==null) {
			this.codes.clear();
			return;
		}
		this.codes = codes;
	}
	

}
