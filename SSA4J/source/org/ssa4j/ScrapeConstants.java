package org.ssa4j;

/**
 * Collection of System Property keys that the Scraper Framework looks 
 * for during various configuration tasks.
 * 
 * @author Rodney Aiglstorfer
 *
 */
public interface ScrapeConstants {
	/**
	 * The Hostname for the Screen-Scraper server
	 */
	public static final String SS_HOST_KEY = "mf.screenscraper.host";
	
	/**
	 * The Port for the Screen-Scrapper server
	 */
	public static final String SS_PORT_KEY = "mf.screenscraper.port";
	
	/**
	 * The timeout value for the RemoteScrapingSession
	 */
	public static final String SS_TIMEOUT_KEY = "mf.screenscraper.timeout";
	
	/**
	 * The base directory within which all mock screen-scraper scenario files are located.
	 */
	public static final String SS_MOCKDIR_KEY = "mf.screenscraper.mockdir";
	
	/**
	 * The classname of the concrete implementation of ScrapeSessionManager
	 */
	public static final String SS_MANAGER_KEY = "mf.screenscraper.manager";
}
