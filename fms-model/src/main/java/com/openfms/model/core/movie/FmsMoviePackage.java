package com.openfms.model.core.movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;
import com.openfms.model.core.references.FmsEventReference;

@Indexes(
		{
			@Index(fields={"projectId","externalId"},name="fmp_index_external_id_per_project", unique=false),
			@Index(fields={"quickhash"},name="fmp_index_quickhash", unique=false),
			@Index(fields={"externalId"},name="fmp_index_externalId", unique=false),
			@Index(fields={"name"},name="fmp_name", unique=false),
			@Index(fields={"size"},name="fmp_size", unique=false),
		}
	)
@FmsAccessControlled(FmsAccessControlled.GROUP.MEDIA)
public class FmsMoviePackage extends AbstractFmsObject {

	private static final long serialVersionUID = -237225549702642849L;

	private String type;
	private String quickhash;
	private String externalId;
	private String movieId;
	private long size;

	// back references
	private List<FmsEventReference> events = new ArrayList<FmsEventReference>();
	private List<String> movieVersionIds = new ArrayList<String>();
	
	private Map<String, Object> data = new HashMap<String, Object>(); 
	
	public FmsMoviePackage() {
		super(null);
	}

	public FmsMoviePackage(String id) {
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

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public List<FmsEventReference> getEvents() {
		if(events == null) {
			events = new ArrayList<FmsEventReference>();
		}
		return events;
	}

	public void setEvents(List<FmsEventReference> events) {
		this.events.clear();
		if(events!=null) {
			this.events.addAll(events);
		}
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public List<String> getMovieVersionIds() {
		return movieVersionIds;
	}

	public void setMovieVersionIds(List<String> movieVersionIds) {
		this.movieVersionIds = movieVersionIds;
	}
	

}
