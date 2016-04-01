package com.openfms.model.core.crypto;

public class FmsKeyParseResult {

	public boolean ok;
	private byte[] data;
	private String filename;
	public FmsKey key;
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	public FmsKey getKey() {
		return key;
	}
	public void setKey(FmsKey key) {
		this.key = key;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
}
