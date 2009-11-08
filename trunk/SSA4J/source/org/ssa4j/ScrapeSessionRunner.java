package org.ssa4j;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ScrapeSessionRunner<T> implements Runnable {

	private T session;
	private ScrapeSessionListener<T> listener;
	private Map<String, String> cookiejar;
	private ScrapeSessionManager manager;
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public ScrapeSessionRunner(T session, ScrapeSessionListener<T> listener, Map<String, String> cookiejar, ScrapeSessionManager manager) {
		this.session = session;
		this.listener = listener;
		this.cookiejar = cookiejar;
		this.manager = manager;
	}
	
	public void run() {		
		String sessionId = manager.getSessionId(session);
		long stime = System.currentTimeMillis();
		log.debug(String.format(">> Starting Scrape Session '%s'", sessionId));
		if (sessionId != null) {
			try {
				if (listener != null) {
					listener.onScrapeReady(session, cookiejar);
				}
				// setup any variables required by the session
				manager.setup(session, cookiejar);
	
				// call the concrete impls execute method
				long time = System.currentTimeMillis();
				manager.execute(session, cookiejar);
	            time = System.currentTimeMillis() - time;
	            log.info(String.format("<<< Scrape Session '%s' completed [%dms]", sessionId, time));
	            
	            // process the DataSet's from the sessions
	            time = System.currentTimeMillis();
	            manager.process(session, cookiejar);
	            time = System.currentTimeMillis() - time;
	            log.info(String.format("<<< Scrape Session '%s' response processed [%dms]", sessionId, time));
	            
	            if (listener != null)
					listener.onScrapeComplete(session, cookiejar);
	            
			} catch (ScrapeSessionTimeoutException e) {
				if (listener != null)
					listener.onScrapeTimeout(session, cookiejar);
				else
					throw new RuntimeException(e);
			} catch (ScrapeException e) {
				if (listener != null)
					listener.onScrapeError(session, cookiejar, e);
				else 
					throw new RuntimeException(e);
			} catch (Exception e) {
				// wrap in a ScrapeException and pass along
				if (listener != null) {
					listener.onScrapeError(session, cookiejar, e);
				} else {
					String msg = String.format("Unexpected problem with session '%s'", sessionId);
					throw new RuntimeException(msg, e);
				}
			} finally {
				// Very important! Be sure to disconnect from the server.
				try {
					manager.close();
				} catch (ScrapeException e) {
					throw new RuntimeException("Unabled to close ScrapeSesssionManager", e);
				}
			}
		} else {
			throw new RuntimeException("Adapter is not correctly annotated");
		}
		stime = System.currentTimeMillis() - stime;
		log.debug(String.format("<< Finished Scrape Session '%s' [%dms]", sessionId, stime));
	}


}
