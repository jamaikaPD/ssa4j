package org.ssa4j;

import java.util.Map;

/**
 * A listener for {@link ScrapeSessionManager} events.
 * @author Rodney Aiglstorfer
 */
public interface ScrapeSessionListener<T> {
	/**
	 * Called once the session has been setup and the session
	 * variable that need to be have been set.
	 */
	public void onScrapeReady(T session, Map<String, String> cookieJar);
	/**
	 * Called when the scrape completes
	 */
	public void onScrapeComplete(T session, Map<String, String> cookieJar);
	/**
	 * Called when scrape session timesout 
	 */
	public void onScrapeTimeout(T session, Map<String, String> cookieJar);
	/**
	 * Called when an Error is found in the scrape session
	 * @param exception
	 */
	public void onScrapeError(T session, Map<String, String> cookieJar, Throwable t);
}
