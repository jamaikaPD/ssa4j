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
	public static final String SSA4J_HOST_KEY = "ssa4j.host";
	
	/**
	 * The Port for the Screen-Scrapper server
	 */
	public static final String SSA4J_PORT_KEY = "ssa4j.port";
	
	/**
	 * The timeout value for the RemoteScrapingSession
	 */
	public static final String SSA4J_TIMEOUT_KEY = "ssa4j.timeout";
	
	/**
	 * The base directory within which all mock screen-scraper scenario files are located.
	 */
	public static final String SSA4J_MOCKDIR_KEY = "ssa4j.mockdir";
	
	/**
	 * The classname of the concrete implementation of ScrapeSessionManager
	 */
	public static final String SSA4J_MANAGER_KEY = "ssa4j.manager";

	/**
	 * The directory where screen-scraper is installed.
	 */
	public static final String SSA4J_SS_HOME_DIR = "ssa4j.screenscraper.home";
	
	/**
	 * The max number of concurrent threads to allow
	 */
	public static final String SSA4J_MAX_THREADS = "ssa4j.max.threads";
}
