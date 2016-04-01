package com.openfms.model.core.scheduledjobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cinefms.dbstore.api.annotations.Index;
import com.cinefms.dbstore.api.annotations.Indexes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openfms.model.Status;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.core.AbstractFmsObject;
import com.openfms.model.utils.StatusCombine;

@Indexes(
		{
			@Index(fields={"status"},name="idxSts",unique=false),
			@Index(fields={"date"},name="idxDt",unique=false),
			@Index(fields={"scheduledJobId"},name="idx_scheduledJobId",unique=false),
		}
	)
@FmsAccessControlled(FmsAccessControlled.GROUP.SCHEDULED_JOB_LOGS)
public class FmsScheduledJobLog extends AbstractFmsObject {

	private static final long serialVersionUID = -4665781919743528187L;
	private String scheduledJobId;
	private int status = Status.NOT_APPLICABLE.value();
	private Date date = new Date();
	private String message;
	private List<FmsScheduledJobLog> children = new ArrayList<FmsScheduledJobLog>();
	private long start=System.currentTimeMillis();
	private long executionTime;
	
	public FmsScheduledJobLog() {
		super(null);
	}

	public FmsScheduledJobLog(String message) {
		super(null);
		this.message = message;
	}
	
	public FmsScheduledJobLog appendMessage(String message) {
		FmsScheduledJobLog out = new FmsScheduledJobLog(message);
		this.addChild(out);
		this.executionTime = System.currentTimeMillis()-start;
		return out;
	}
	
	@JsonIgnore
	public FmsScheduledJobLogBrief getBrief() {
		return new FmsScheduledJobLogBrief(this);
	}
	
	private void addChild(FmsScheduledJobLog log) {
		this.children.add(log);
	}

	public void appendLog(FmsScheduledJobLog log) {
		this.addChild(log);
		this.executionTime = System.currentTimeMillis()-start;
	}
	
	public String getScheduledJobId() {
		return scheduledJobId;
	}

	public void setScheduledJobId(String scheduledJobId) {
		this.scheduledJobId = scheduledJobId;
	}

	public int getStatus() {
		int out = status;
		for(FmsScheduledJobLog l : children) {
			if(l!=null) {
				out = StatusCombine.combine(out,l.getStatus());
			}
		}
		return out;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<FmsScheduledJobLog> getChildren() {
		return children;
	}

	public void setChildren(List<FmsScheduledJobLog> children) {
		this.children = children;
	}

	

}
