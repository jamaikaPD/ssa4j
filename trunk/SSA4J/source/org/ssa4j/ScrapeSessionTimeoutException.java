package org.ssa4j;

/**
 * Exception called when a timeout occurs during a scrape.
 * 
 * @author Rodney Aiglstorfer
 */
public class ScrapeSessionTimeoutException extends ScrapeException {

	private static final long serialVersionUID = -3914346658023753970L;
	
	private String sessionId;
	
	public ScrapeSessionTimeoutException(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**
	 * The name of the {@link ScrapeSession} that timed out.
	 * @return
	 */
	public String getSessionId() {
		return sessionId;
	}
	

}
