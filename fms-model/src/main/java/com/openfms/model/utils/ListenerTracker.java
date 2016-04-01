package com.openfms.model.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cinefms.dbstore.api.DBStoreEntity;
import com.openfms.utils.common.text.StringUtil;

public class ListenerTracker {
	
	private static Log log = LogFactory.getLog(ListenerTracker.class);

	private long start=System.currentTimeMillis();
	private long count=0;
	
	private Map<String,DBStoreEntity> visited = new HashMap<String, DBStoreEntity>();
	
	public boolean add(DBStoreEntity o) {

		count++;
		if(count>100000) {
			throw new RuntimeException("thread timeout: did too many iterations! （"+o.getClass()+" / "+o.getId()+") "+StringUtil.getCurrentStack());
		}
		
		if(System.currentTimeMillis()-start > 1000*60*10) {
			throw new RuntimeException("thread timeout: thread took too long to finish! （"+o.getClass()+" / "+o.getId()+") "+StringUtil.getCurrentStack());
		}
		
		String key = o.getClass()+":"+o.getId();
		DBStoreEntity x = visited.get(key);
		if(x==null) {
			visited.put(key, o);
			log.info(Thread.currentThread().getName()+" =====> ADDING: "+key+"  ("+visited.size()+")");
			return true;
		}
		return false;
	}
	
	public void remove(DBStoreEntity o) {
		//String key = o.getClass()+":"+o.getId();
		//visited.remove(key);
		//log.info(Thread.currentThread().getName()+" <===== REMOVING: "+key+"  ("+visited.size()+")");
		
	}
	
	
}
