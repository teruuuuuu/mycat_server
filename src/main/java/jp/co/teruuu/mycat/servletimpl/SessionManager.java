package jp.co.teruuu.mycat.servletimpl;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class SessionManager {
	private final ScheduledExecutorService scheduler;
	@SuppressWarnings("unused")
	private final ScheduledFuture<?> cleanerHandle;
	private final int CLEAN_INTERVAL = 60;
	private final int SESSION_TIMEOUT = 10;
	private Map<String, HttpSessionImpl> sessions = new ConcurrentHashMap<String, HttpSessionImpl>();
	private SessionIdGenerator sessionIdGenerator;
	
	synchronized HttpSessionImpl getSession(String id){
		HttpSessionImpl ret = sessions.get(id);
		if ( ret != null){
			ret.access();
		}
		return ret;
	}
	
	HttpSessionImpl createSession() {
		String id = this.sessionIdGenerator.generateSessionId();
		HttpSessionImpl session = new HttpSessionImpl(id);
		sessions.put(id, session);
		return session;
	}
	
	private synchronized void cleanSessions() {
		for(Iterator<String> it = sessions.keySet().iterator(); it.hasNext();){
			String id = it.next();
			HttpSessionImpl session = this.sessions.get(id);
			if(session.getlastAccessedTime() < System.currentTimeMillis() - (SESSION_TIMEOUT * 60 * 10000)){
				it.remove();
			}
		}
	}
	
	SessionManager() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		Runnable cleaner = new Runnable() {
			public void run() {
				cleanSessions();
			}
		};
		
		this.cleanerHandle = scheduler.scheduleWithFixedDelay(cleaner, CLEAN_INTERVAL, CLEAN_INTERVAL, TimeUnit.SECONDS	);
		this.sessionIdGenerator = new SessionIdGenerator();
		
	}
}
