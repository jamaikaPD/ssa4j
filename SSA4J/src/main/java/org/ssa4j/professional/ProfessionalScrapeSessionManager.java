package org.ssa4j.professional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ssa4j.ScrapeConstants;
import org.ssa4j.ScrapeException;
import org.ssa4j.ScrapeSessionManager;
import org.ssa4j.ScrapeSessionTimeoutException;

import com.screenscraper.common.DataSet;
import com.screenscraper.scraper.RemoteScrapingSession;
import com.screenscraper.scraper.RemoteScrapingSessionException;

/**
 * Implementation of ScrapeSessionManager that relies on Scrape annotations to 
 * create a RemoteScrapingSession connection and execute scraping session.
 * <p>
 * This ScrapeSessionManager supports the following optional System properties
 * <dl>
 * <dt>mf.screenscraper.host</dt>
 * <dd>The Hostname for the Screen-Scraper server.  Defaults to "localhost"</dd>
 * <dt>mf.screenscraper.port</dt>
 * <dd>The Port for the Screen-Scrapper server.  Defaults to "8778"</dd>
 * <dt>mf.screenscraper.timeout</dt>
 * <dd>Sets the number of minutes a scraping session should be allowed to run 
 * 		before it automatically stops itself. The timeout value is in minutes.
 * 		if not defined, default is 1 minute. 
 * </dd>
 * </dl>
 * 
 * @author Rodney Aiglstorfer
 *
 */
public class ProfessionalScrapeSessionManager extends ScrapeSessionManager {
	
	private static final ThreadLocal<RemoteScrapingSession> remoteSession = new ThreadLocal<RemoteScrapingSession>();
	
	private String scraperHost;
	private int scraperPort;
	private int timeout;
	
	private static final ThreadLocal<HashMap<String, String>> sessionVariables = new ThreadLocal<HashMap<String, String>>() {

		@Override
		protected HashMap<String, String> initialValue() {
			return new HashMap<String, String>();
		}
		
	};

	/**
	 * Creates a RemoteScrapeSessionManager using the default settings 
	 * for host, port, and timeout.
	 */
	public ProfessionalScrapeSessionManager() {
		super();
		scraperHost = System.getProperty(ScrapeConstants.SSA4J_HOST_KEY, "localhost");
		scraperPort = Integer.parseInt(System.getProperty(ScrapeConstants.SSA4J_PORT_KEY, "8778"));
		timeout = Integer.parseInt(System.getProperty(ScrapeConstants.SSA4J_TIMEOUT_KEY, "1")); 
	}
	
	/**
	 * Creates a RemoteScrapeSessionManager using the specified settings for
	 * host, port, and timeout
	 * @param scraperHost The screen-scraper server hostname
	 * @param scraperPort The screen-scraper server port
	 * @param timeout The timeout for the scrape session
	 * 
	 * @throws ScrapeException 
	 */
	public ProfessionalScrapeSessionManager(String scraperHost, int scraperPort, int timeout) throws ScrapeException {
		this.scraperHost = scraperHost;
		this.scraperPort = scraperPort;
		this.timeout = timeout;
	}

	@Override
	public void close() throws ScrapeException {
		try {
			if (remoteSession.get() != null) {
				remoteSession.get().disconnect();
				remoteSession.remove();
			}
			sessionVariables.remove();
		} catch (IOException e) {
			throw new ScrapeException("Error trying to close scraping session");
		}
	}

	@Override
	protected void execute(Object source, Map<String, String> cookiejar) throws ScrapeException {
		try {
			String sessionId = getSessionId(source);
			log.info(String.format(">> Connecting to Screen-Scraper '%s:%d' ", scraperHost, scraperPort));
			remoteSession.set(new RemoteScrapingSession(sessionId, scraperHost, scraperPort));
			for (Entry<String, String> entry : sessionVariables.get().entrySet()) {
				remoteSession.get().setVariable(entry.getKey(), entry.getValue());
			}
			
			remoteSession.get().setTimeout(timeout);
			
			remoteSession.get().scrape();
			
			if (remoteSession.get().sessionTimedOut()) {
				throw new ScrapeSessionTimeoutException(sessionId);
			}
			
		} catch (RemoteScrapingSessionException e) {
			throw new ScrapeException("Remote Scraping Session Error", e);
		} catch (IOException e) {
			throw new ScrapeException("Unexpected IO Error", e);
		}
	}

	

	@Override
	protected DataSet getDataSet(String name) throws ScrapeException {
		try {
			Object obj = remoteSession.get().getVariable(name);
			return (obj instanceof DataSet)?  (DataSet) obj : null;
		} catch (RemoteScrapingSessionException e) {
			log.warn(String.format("Problem getting variable %s", name), e);
			return null;
		}
	}

	@Override
	protected void setVariable(String name, String value) throws ScrapeException {
		sessionVariables.get().put(name, value);
	}

	@Override
	protected String getVariable(String name) throws ScrapeException {
		try {
			return (String) remoteSession.get().getVariable(name);
		} catch (RemoteScrapingSessionException e) {
			log.warn(String.format("Problem getting variable %s", name), e);
			return null;
		}
	}
	
	
	
}
