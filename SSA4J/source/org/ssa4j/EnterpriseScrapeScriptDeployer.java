package org.ssa4j;

import java.io.IOException;
import java.net.URL;

import com.screenscraper.soapclient.SOAPInterface;
import com.screenscraper.soapclient.SOAPInterfaceService;
import com.screenscraper.soapclient.SOAPInterfaceServiceLocator;

public class EnterpriseScrapeScriptDeployer extends ScrapeScriptDeployer {
	
	SOAPInterface soap;
	
	public EnterpriseScrapeScriptDeployer() throws ScrapeException {
		// "http://localhost:8779/axis/services/SOAPInterface";
		String host = System.getProperty(ScrapeConstants.SS_HOST_KEY, "localhost");
		int port = Integer.parseInt(System.getProperty(ScrapeConstants.SS_PORT_KEY, "8779"));
		int timeout = Integer.parseInt(System.getProperty(ScrapeConstants.SS_TIMEOUT_KEY, "1")); 
		
		String endpoint = String.format("http://%s:%d/axis/services/SOAPInterface", host, port);
		
		this.initialize(endpoint, timeout);
	}
	
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
