package org.ssa4j;

public interface ScrapeSessionListener {
	public void onScrapeReady();
	public void onScrapeComplete();
	public void onScrapeTimeout();
	public void onScrapeError(ScrapeSessionException exception);
}
