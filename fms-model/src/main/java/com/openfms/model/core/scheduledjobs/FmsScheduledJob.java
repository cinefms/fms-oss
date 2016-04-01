package com.openfms.model.core.scheduledjobs;


public interface FmsScheduledJob {

	public FmsScheduledJobLog run();
	
	public void end();

}
