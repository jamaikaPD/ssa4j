package org.ssa4j.enterprise;

import java.io.IOException;
import java.net.URL;

import org.ssa4j.ScrapeConstants;
import org.ssa4j.ScrapeException;
import org.ssa4j.ScrapeScriptDeployer;
import org.ssa4j.ScrapeSessionManager;

import com.screenscraper.soapclient.SOAPInterface;
import com.screenscraper.soapclient.SOAPInterfaceService;
import com.screenscraper.soapclient.SOAPInterfaceServiceLocator;

/**
 * Implementation of  {@link ScrapeSessionManager} that wraps the 
 * screen-scraper SOAP APIs that are exposed as part of the Enterprise
 * edition of screen-scraper.
 * 
 * @author Rodney Aiglstorfer
 */
public class EnterpriseScrapeScriptDeployer extends ScrapeScriptDeployer {
	
	SOAPInterface soap;
	
	/**
	 * Default constructor pulls its values for localhost, port, and timeout from 
	 * System properties.  
	 * @see ScrapeConstants#SSA4J_HOST_KEY
	 * @see ScrapeConstants#SSA4J_PORT_KEY
	 * @see ScrapeConstants#SSA4J_TIMEOUT_KEY
	 * @throws ScrapeException
	 */
	public EnterpriseScrapeScriptDeployer() throws ScrapeException {
		this(System.getProperty(ScrapeConstants.SSA4J_HOST_KEY, "localhost"), 
				Integer.parseInt(System.getProperty(ScrapeConstants.SSA4J_PORT_KEY, "8779")), 
				Integer.parseInt(System.getProperty(ScrapeConstants.SSA4J_TIMEOUT_KEY, "1")));
		
	}
	
	/**
	 * Explicit constructor that takes all the required configuration properties
	 * @param host The hostname for the screen-scraper server
	 * @param port The port for the screen-scraper server
	 * @param timeout The time out in minutes
	 * @throws ScrapeException
	 */
	public EnterpriseScrapeScriptDeployer(String host, int port, int timeout) throws ScrapeException {
		String endpoint = String.format("http://%s:%d/axis/services/SOAPInterface", host, port);
		
		this.initialize(endpoint, timeout);
	}
	
	/**
	 * Explicit constructor that takes an endpoint for the scrape-session SOAP services.  Only use this
	 * if you have somehow changed the default endpoint.
	 * @param endpoint
	 * @param timeout
	 * @throws ScrapeException
	 */
	public EnterpriseScrapeScriptDeployer(String endpoint, int timeout) throws ScrapeException {
		this.initialize(endpoint, timeout);
	}

	@Override
	protected void deploy(String name, byte[] data) throws IOException {
		soap.update(new String(data, "UTF-8"));
	}
	
	private void initialize(String endpoint, int timeout) throws ScrapeException {
		SOAPInterfaceService service = new SOAPInterfaceServiceLocator();
		
		try {
			log.info(String.format("Endpoint: %s, timeout: %d", endpoint, timeout));
			this.soap = service.getSOAPInterface(new URL(endpoint));
		} catch (Exception e) {
			throw new ScrapeException("Error creating SoapScrapeSessionManager", e);
		}
	}

}
