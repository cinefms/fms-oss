package com.openfms.utils.debug;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.openfms.utils.common.text.StringUtil;

public class ThreadDumper {
	
	private static Log log = LogFactory.getLog("THREADS");
	private ThreadMXBean tmx;
	
	@PostConstruct
	public void init() {
		tmx = ManagementFactory.getThreadMXBean(); 
	}
	
	public void dump() {
        final ThreadInfo[] threads = tmx.dumpAllThreads(true, true);

        for (int ti = threads.length - 1; ti >= 0; ti--) {
            final ThreadInfo t = threads[ti];
            
            log.info(" ======================================================================================================================= ");
            log.info(" == "+
	            String.format("%s id=%d state=%s running=%d",
	                          t.getThreadName(),
	                          t.getThreadId(),
	                          t.getThreadState(),
	                          (tmx.getThreadCpuTime(t.getThreadId())/1000000000)
	                          
	            	)
            );
            final LockInfo lock = t.getLockInfo();
            if (lock != null && t.getThreadState() != Thread.State.BLOCKED) {
                log.info(" == "+
	                String.format("    - waiting on <0x%08x> (a %s)",
	                              lock.getIdentityHashCode(),
	                              lock.getClassName())
                );
                log.info(" == "+
	                String.format("    - locked <0x%08x> (a %s)",
	                              lock.getIdentityHashCode(),
	                              lock.getClassName())
                        );
            } else if (lock != null && t.getThreadState() == Thread.State.BLOCKED) {
                log.info(" == "+
	                String.format("    - waiting to lock <0x%08x> (a %s)",
	                              lock.getIdentityHashCode(),
	                              lock.getClassName())
	            );
            }

            if (t.isSuspended()) {
            	log.info(" == (suspended)");
            }

            if (t.isInNative()) {
            	log.info(" == (running in native)");
            }

            if (t.getLockOwnerName() != null) {
                log.info(" == "+
                		String.format("     owned by %s id=%d", t.getLockOwnerName(), t.getLockOwnerId())
                );
            }

            final StackTraceElement[] elements = t.getStackTrace();
            final MonitorInfo[] monitors = t.getLockedMonitors();

            for (int i = 0; i < elements.length; i++) {
                final StackTraceElement element = elements[i];
                log.info(" == "+
                		String.format("    at %s", element)
                );
                for (int j = 1; j < monitors.length; j++) {
                    final MonitorInfo monitor = monitors[j];
                    if (monitor.getLockedStackDepth() == i) {
                        log.info(" == "+
                        		String.format("      - locked %s", monitor)
                        );
                    }
                }
            }

            final LockInfo[] locks = t.getLockedSynchronizers();
            if (locks.length > 0) {
                log.info(" == "+
	                String.format("    Locked synchronizers: count = %d", locks.length)
                );
                for (LockInfo l : locks) {
                    log.info(" == "+
                    		String.format("      - %s", l)
                    );
                }
            }
        }
	}
	
	
	public void dump2() {
		try {
			Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
			log.info(" =================================================================================================== ");
			for(Thread t : threadSet) {
				try {
					log.info(" =================================================================================================== ");
					log.info(" === ----- "+t.getState()+" --- "+t.getPriority()+" --- "+(t.getThreadGroup()!=null?t.getThreadGroup().getName():"[null]")+" / "+t.getName()+" / ");
					log.info(" === \n"+StringUtil.getStackTrace(t.getStackTrace()));
					log.info(" =================================================================================================== ");
				} catch (Exception e) {
					log.error("error dumping stack: ",e);
				}
			}
			log.info(" =================================================================================================== ");
			
		} catch (Exception e) {
		}
	}
	

}
