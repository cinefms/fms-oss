package com.openfms.core.service.impl.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.openfms.core.service.admin.impl.auth.FmsAdminUser;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.FmsSession;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.utils.ListenerTrackerHolder;
import com.openfms.utils.common.reflection.ReflectionSupports;
import com.skjlls.aspects.metrics.annotations.Metrics;

public abstract class FmsListenerAdapter<T> implements  FmsListener {

	private static Log log = LogFactory.getLog(FmsListenerAdapter.class);
	
	protected FmsListenerAdapter() {
	}

	@Override
	public boolean supports(Class<? extends FmsObject> clazz) {
		return ReflectionSupports.supports(getClass(), clazz);
	}
	
	protected FmsSession runAs() {
		FmsUser u = FmsSessionHolder.getCurrentUser();
		FmsSession old = null; 
		if(u==null || !u.isAdmin()) {
			old = FmsSessionHolder.get();
			FmsUser user = new FmsAdminUser();
			user.setName("[SYSTEM] ("+(u!=null?u.getName():"anon")+")");
			user.setDisplayName("System User ("+(u!=null?u.getDisplayName():"anon")+")");
			FmsSession session = new FmsSession();
			session.setUser(user);
			FmsSessionHolder.set(session);
		}
		return old;
	}
	
	@Override
	@Metrics(value="save.beforeSave",count=true,time=true,error=false)
	public final boolean beforeSave(String db, FmsObject o) {
		if(o.getId()== null ) {
			// continue anyway, it's new
		} else if(!ListenerTrackerHolder.add(o)) {
			log.info(" --- already visited: "+o.getClass()+"/"+o.getName()+"/"+o.getId()+" ... aborting ");
			return false;
		} else {
			log.info(" --- not visited yet: "+o.getClass()+"/"+o.getName()+"/"+o.getId()+" ... going there ");
		}
		FmsSession session = runAs();
		try {
			return beforeSave(db, (T)o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			ListenerTrackerHolder.remove(o);
			if(session!=null) {
				FmsSessionHolder.set(session);
			}
		}
	}

	@Override
	@Metrics(value="save.updated",count=true,time=true,error=false)
	public final void updated(String db, FmsObject o, FmsObject n) {
		FmsSession session = runAs();
		try {
			runAs();
			updated(db, (T)o, (T)n);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			ListenerTrackerHolder.remove(o);
			if(session!=null) {
				FmsSessionHolder.set(session);
			}
		}
	}

	@Override
	@Metrics(value="save.beforeDelete",count=true,time=true,error=false)
	public final void beforeDelete(String db, FmsObject o) {
		if(!ListenerTrackerHolder.add(o)) {
			log.info(" --- already visited: "+o.getClass()+"/"+o.getName()+"/"+o.getId()+" ... aborting ");
			return;
		} else {
			log.info(" --- not visited yet: "+o.getClass()+"/"+o.getName()+"/"+o.getId()+" ... going there ");
		}
		FmsSession session = runAs();
		try {
			beforeDelete(db, (T)o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			ListenerTrackerHolder.remove(o);
			if(session!=null) {
				FmsSessionHolder.set(session);
			}
		}
	}

	@Override
	@Metrics(value="save.deleted",count=true,time=true,error=false)
	public final void deleted(String db, FmsObject o) {
		FmsSession session = runAs();
		try {
			deleted(db, (T)o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			ListenerTrackerHolder.remove(o);
			if(session!=null) {
				FmsSessionHolder.set(session);
			}
		}
	}

	@Override
	@Metrics(value="save.created",count=true,time=true,error=false)
	public final void created(String db, FmsObject o) {
		if(!ListenerTrackerHolder.add(o)) {
			log.info(" --- already visited: "+o.getClass()+"/"+o.getName()+"/"+o.getId()+" ... aborting ");
			return;
		} else {
			log.info(" --- not visited yet: "+o.getClass()+"/"+o.getName()+"/"+o.getId()+" ... going there ");
		}
		FmsSession session = runAs();
		try {
			created(db, (T)o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			ListenerTrackerHolder.remove(o);
			if(session!=null) {
				FmsSessionHolder.set(session);
			}
		}
	}
	
	protected boolean beforeSave(String db, T object) {
		return true;
	}
	
	protected void beforeDelete(String db, T object) {
	}
	
	protected void created(String db, T object) {
	}

	protected void updated(String db, T o, T n) {
	}
	
	protected void deleted(String db, T object) {
	}

}
