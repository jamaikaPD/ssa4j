package org.ssa4j;

public class ScrapeSessionTimeoutException extends ScrapeException {

	private static final long serialVersionUID = -3914346658023753970L;
	
	private String sessionId;
	
	public ScrapeSessionTimeoutException(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	

}
