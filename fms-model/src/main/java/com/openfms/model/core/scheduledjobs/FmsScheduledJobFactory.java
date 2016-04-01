package com.openfms.model.core.scheduledjobs;

import java.util.List;

public interface FmsScheduledJobFactory {

	public List<FmsScheduledJobType> getTypes(); 

	public boolean supports(String type); 
	
	public FmsScheduledJob getScheduledJob(FmsScheduledJobConfig jobConfig, FmsScheduledJobClientConfig clientConfig);

	
	
}
