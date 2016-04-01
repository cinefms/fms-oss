package com.openfms.model.core.movie;

import java.util.HashMap;
import java.util.Map;

import com.cinefms.apitester.annotations.ApiDescription;
import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;


@Indexes(
	{
		@Index(fields={"projectId","externalId"},name="ff_unique_external_id_per_project", unique=false),
		@Index(fields={"quickhash"},name="ff_index_quickhash", unique=false),
		@Index(fields={"md5"},name="ff_index_md5", unique=false),
		@Index(fields={"type"},name="ff_index_type", unique=false),
		@Index(fields={"path"},name="ff_index_path", unique=false),
		@Index(fields={"packageId"},name="ff_packageId", unique=false),
		@Index(fields={"externalId"},name="ff_externalId", unique=false),
		@Index(fields={"name"},name="ff_name", unique=false)
	}
)
@ApiDescription(file="FmsFile.md")
@FmsAccessControlled(FmsAccessControlled.GROUP.MEDIA)
public class FmsFile extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	private String type;
	private String quickhash;
	private String md5;
	private String externalId;
	private String packageId;
	private String path;
	private long size;
	private boolean encrypted;
	private Map<String,Object> data = new HashMap<String, Object>(); 
	
	public FmsFile() {
		super(null);
	}

	public FmsFile(String id) {
		super(id);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQuickhash() {
		return quickhash;
	}

	public void setQuickhash(String quickhash) {
		this.quickhash = quickhash;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public Map<String,Object> getData() {
		return data;
	}

	public void setData(Map<String,Object> data) {
		this.data = data;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

}
