package org.ssa4j;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.screenscraper.common.DataRecord;
import com.screenscraper.common.DataSet;
import com.screenscraper.soapclient.SOAPInterface;
import com.screenscraper.soapclient.SOAPInterfaceService;
import com.screenscraper.soapclient.SOAPInterfaceServiceLocator;

public class EnterpriseScrapeSessionManager extends ScrapeSessionManager {
	
	// Necessary calls to auto-generated classes.
	SOAPInterface soap;
	String soapSessionId;
	int timeout = 1;
	
	Map<String, String> sessionVariables = new HashMap<String, String>();
	Map<String, DataSet> dataSetCache = new HashMap<String, DataSet>();
	
	public EnterpriseScrapeSessionManager() throws ScrapeException {
		// "http://localhost:8779/axis/services/SOAPInterface";
		String host = System.getProperty(ScrapeConstants.SS_HOST_KEY, "localhost");
		int port = Integer.parseInt(System.getProperty(ScrapeConstants.SS_PORT_KEY, "8779"));
		int timeout = Integer.parseInt(System.getProperty(ScrapeConstants.SS_TIMEOUT_KEY, "1")); 
		
		String endpoint = String.format("http://%s:%d/axis/services/SOAPInterface", host, port);
		
		this.initialize(endpoint, timeout);
	}
	
	public EnterpriseScrapeSessionManager(String endpoint, int timeout) throws ScrapeException {
		this.initialize(endpoint, timeout);
	}
	
	private void initialize(String endpoint, int timeout) throws ScrapeException {
		SOAPInterfaceService service = new SOAPInterfaceServiceLocator();
		
		try {
			log.info(String.format("Endpoint: %s, timeout: %d", endpoint, timeout));
			this.soap = service.getSOAPInterface(new URL(endpoint));
			this.timeout = timeout;
		} catch (Exception e) {
			throw new ScrapeException("Error creating SoapScrapeSessionManager", e);
		}
	}

	@Override
	protected void close() throws ScrapeException {
		try {
			// Clean up memory usage by screen-scraper.
			soap.removeCompletedScrapingSession(this.soapSessionId);
			soap = null;
			soapSessionId = null;
		} catch (RemoteException e) {
			throw new ScrapeException("Error closing session", e);
		}
	}

	@Override
	protected void execute(Object source, CookieJar cookiejar)
			throws ScrapeException {
		String sessionId = getSessionId(source);
		
		try {
			// Initialize the scraping session.
			soapSessionId = soap.initializeScrapingSession(sessionId);
			
			// set the timeout for the session
			soap.setTimeout(soapSessionId, timeout);
			
			// Set all session variables
			for (Entry<String, String> entry : sessionVariables.entrySet()) {
				soap.setVariable(this.soapSessionId, entry.getKey(), entry.getValue());
			}

			// Start scraping. This call returns immediately,
			// though the scraping session is not completed.
			soap.scrape(soapSessionId);

			// Wait until scraping completes.
			while (soap.isFinished(soapSessionId) != 1) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ignored) {
				}
			}
			
		} catch (RemoteException e) {
			throw new ScrapeException("Error executing session", e);
		}

	}

	@Override
	protected DataRecord getDataRecordFromDataSet(String id, int ndx)
			throws ScrapeException {
		DataSet ds = loadDataSet(id);
		return (ds != null)? ds.getDataRecord(ndx) : null;
	}

	@Override
	protected int getNumDataRecordsInDataSet(String id) throws ScrapeException {
		DataSet ds = loadDataSet(id);
		return (ds != null)? ds.getNumDataRecords() : 0;
	}

	@Override
	protected String getVariable(String name) throws ScrapeException {
		try {
			return soap.getVariable(this.soapSessionId, name);
		} catch (RemoteException e) {
			throw new ScrapeException("Error getting variable " + name, e);
		}
	}

	@Override
	protected void setVariable(String name, String value)
			throws ScrapeException {
		sessionVariables.put(name, value);
	}
	
	protected DataSet loadDataSet(String dataSetId) throws ScrapeException {
		
		DataSet ds = null;
		try {
			if (dataSetCache.containsKey(dataSetId)) {
				ds = dataSetCache.get(dataSetId);
			} else {
				String[][] rawDataSet = soap.getDataSet(this.soapSessionId, dataSetId);
				ds = createDataSet(rawDataSet);
				dataSetCache.put(dataSetId, ds);
			}
		} catch (RemoteException e) {
			throw new ScrapeException("Error loading DataSet " + dataSetId, e);
		}
		
		return ds;
	}
	
	protected DataSet createDataSet(String[][] rawDataSet) {
		DataSet ds = new DataSet();
		for (String[] rawRec : rawDataSet) {
			ds.addDataRecord(createDataRecord(rawRec));
		}
		return ds;
	}
	
	protected DataRecord createDataRecord(String[] rawRec) {
		DataRecord rec = new DataRecord();
		for (String rawField : rawRec) {
			String[] keyval = rawField.split("=");
			rec.put(keyval[0], keyval[1]);
		}
		return rec;
	}

}
