package org.ssa4j;

/**
 * A listener for {@link ScrapeSessionManager} events.
 * @author Rodney Aiglstorfer
 */
public interface ScrapeSessionListener {
	/**
	 * Called once the session has been setup and the session
	 * variable that need to be have been set.
	 */
	public void onScrapeReady();
	/**
	 * Called when the scrape completes
	 */
	public void onScrapeComplete();
	/**
	 * Called when scrape session timesout 
	 */
	public void onScrapeTimeout();
	/**
	 * Called when an Error is found in the scrape session
	 * @param exception
	 */
	public void onScrapeError(ScrapeSessionException exception);
}
