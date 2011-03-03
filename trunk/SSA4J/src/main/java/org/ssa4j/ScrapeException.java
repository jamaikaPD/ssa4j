package org.ssa4j;

/**
 * Wrapper exception for various exception that are thrown by the various systems
 * used by the ScrapeSessionManager.
 * 
 * @author Rodney Aiglstorfer
 *
 */
public class ScrapeException extends Exception {

	private static final long serialVersionUID = 9049323033767044393L;

	public ScrapeException() {
		super();
	}

	public ScrapeException(String message) {
		super(message);
	}

	public ScrapeException(Throwable cause) {
		super(cause);
	}

	public ScrapeException(String message, Throwable cause) {
		super(message, cause);
	}

}
