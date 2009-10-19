package org.ssa4j.mock;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.ssa4j.ScrapeConstants;
import org.ssa4j.ScrapeException;
import org.ssa4j.ScrapeSessionManager;

import com.screenscraper.common.DataRecord;
import com.screenscraper.common.DataSet;

/**
 * Creates a Mock version of a ScrapeSessionManager that enables for better
 * testing and development of solutions that would otherwise be held up 
 * pending a *real* scraping session to test against.  This is also useful
 * for creating and testing boundary cases that may be impossible to create
 * against a *real* system.
 * <p>
 * <h2>Configuring a MockScrapeSessionManager</h2>
 * Before you can use this class you need to do some setup for your environment.
 * <ol>
 * <li>Create a directory somewhere on your system that will become the location
 * for all the mock data files you are going to create for your objects.</li>
 * <li>Create mock data files for every Scraping Session that you want to mock</li>
 * </ol>
 * </p>
 * <em>NOTE</em>: For a complete overview of the structure of a mock data file please
 * see the package notes. 
 * 
 * 
 * @author Rodney Aiglstorfer
 *
 */
public class MockScrapeSessionManager extends ScrapeSessionManager {
	
	private HashMap<String, DataSet> datasets = new HashMap<String, DataSet>();
	private Map<String, String> variables;
	private File scenarioDirectory;
	
	public MockScrapeSessionManager() {
		scenarioDirectory = new File(System.getProperty(ScrapeConstants.SS_MOCKDIR_KEY, "."));
		log.info("Repository for mock session scenarios set to " + scenarioDirectory.getAbsolutePath());
	}
	
	public MockScrapeSessionManager(File scenarioDirectory) {
		this.scenarioDirectory = scenarioDirectory;
	}

	@Override
	protected void close() throws ScrapeException {
		datasets.clear();
	}

	@Override
	protected void execute(Object source, Map<String,String> cookiejar) throws ScrapeException {
		String sessionId = getSessionId(source);
		Serializer serializer = new Persister();
		
		String filename = String.format("%s.xml", sessionId);
		log.info("Loading " + filename);
		
		File f = new File(scenarioDirectory, filename);	
		try {
			
			InputStream in;
			
			if (f.exists()) {
				log.info("Loading from filesystem");
				in = new FileInputStream(f);
			} else {
				log.info("Loading from classpath");
				filename = "/"+filename;
				in = getClass().getResourceAsStream(filename);
				if (in == null) {
					throw new ScrapeException(
							String.format("Resource '%s' not found in classpath.", filename));
				}
			}
			
			MockSession mockSession = serializer.read(MockSession.class, in);
			log.debug(String.format("Number of Scenarios: %d", mockSession.scenarios.size()));
			for (MockScenario scenario : mockSession.scenarios) {
				log.debug("-------------- scenario -------------\n\n\n");
				if (test(scenario.testScript, source, cookiejar)) {
					for (MockDataSet mockdataset : scenario.datasets) {
						DataSet dataset = new DataSet();
						for (MockDataRecord mockRecord : mockdataset.datarecords) {
							log.debug(mockRecord.toString());
							DataRecord datarec = new DataRecord();
							for (Entry<String, String> entry : mockRecord.map.entrySet()) {
								datarec.put(entry.getKey(), entry.getValue());
							}
							dataset.addDataRecord(datarec);
						}
						datasets.put(mockdataset.id, dataset);
					}
					if (scenario.variables != null)
						variables = scenario.variables.map;
					return;
				}
			}
		} catch (Exception e) {
			throw new ScrapeException(String.format("Failed to Mock scrape %s", sessionId), e);
		}
	}

	@Override
	protected DataRecord getDataRecordFromDataSet(String id, int ndx)
			throws ScrapeException {
		DataSet ds = datasets.get(id);
		if (ds == null) {
			log.warn(String.format("No DataSet found for id:'%s'", id));
			return null;
		}
		return ds.getDataRecord(ndx);
	}

	@Override
	protected int getNumDataRecordsInDataSet(String id) throws ScrapeException {
		DataSet ds = datasets.get(id);
		if (ds == null) {
			log.warn(String.format("No DataSet found for id:'%s'", id));
			return 0;
		}
		return ds.getNumDataRecords();
	}

	@Override
	protected void setVariable(String name, String value)
			throws ScrapeException 
	{
		/* Ignored */
	}
	
	protected String getVariable(String name) throws ScrapeException {
		if (variables != null)
			return (String) variables.get(name);
		return null;
	}
	
	/**
	 * Executes the script located within the &lt;test&gt;&lt;/test&gt; tags.
	 * @param script Logic that must return a boolean.
	 * @param source The source session object that is passinto the script as a binding 
	 * called 'session'
	 * @return Returns true if script logic returns true, false otherwise.
	 */
	private Boolean test(String script, Object source, Map<String,String> cookiejar) {
		log.debug(String.format("Running Script {\n%s\n}", script));
		Binding binding = new Binding();
		binding.setVariable("cookiejar", cookiejar);
		binding.setVariable("session", source);
		GroovyShell shell = new GroovyShell(binding);

		Object result = shell.evaluate(script);
		log.debug("Script result: " + result);
		return (Boolean) result;
	}
	
}
