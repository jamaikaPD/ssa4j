package org.ssa4j.mock;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.ssa4j.ScrapeConstants;
import org.ssa4j.ScrapeException;
import org.ssa4j.ScrapeSessionManager;
import org.ssa4j.mock.schema.MockDataRecord;
import org.ssa4j.mock.schema.MockDataRecordField;
import org.ssa4j.mock.schema.MockDataSet;
import org.ssa4j.mock.schema.MockScenario;
import org.ssa4j.mock.schema.MockScript;
import org.ssa4j.mock.schema.MockSession;
import org.ssa4j.mock.schema.MockVariable;

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
	private Map<String, String> variables = new HashMap<String, String>();
	private File scenarioDirectory;
	
	public MockScrapeSessionManager() {
		scenarioDirectory = new File(System.getProperty(ScrapeConstants.SSA4J_MOCKDIR_KEY, "."));
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
	protected void execute(Object session, Map<String,String> cookiejar) throws ScrapeException {
		String sessionId = getSessionId(session);
		
		String filename = String.format("%s.xml", sessionId);
		log.info("Loading " + filename);
		
		File f = new File(scenarioDirectory, filename);	
		InputStream in;
		
		if (f.exists()) {
			log.info("Loading from filesystem");
			try {
				in = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				throw new MockScrapeException("Unabled to load MockSession file", e);
			}
		} else {
			log.info("Loading from classpath");
			filename = "/"+filename;
			in = getClass().getResourceAsStream(filename);
			if (in == null) {
				throw new MockScrapeException(String.format("Resource '%s' not found in classpath.", filename));
			}
		}
		
		MockSession mockSession;
		try {
			mockSession = MockObject.toObject(MockSession.class, in);
		} catch (Exception e) {
			throw new ScrapeException(String.format("Failed to load MockSession file %s", sessionId), e);
		}
		log.debug(String.format("Number of Scenarios: %d", mockSession.scenarios.size()));
		if (mockSession.scenarios.size() > 0) {
			MockScript script = mockSession.script;
			if (script != null) {
				// TODO Add support for other languages using the scripting extensions to Java
				if ("groovy".equals(script.lang)) {
					Object result = runScript(script.script, session, cookiejar);
					if (result != null) {
						for (MockScenario scenario : mockSession.scenarios) {
							if (scenario.id.equals(result)) {
								processScenario(scenario);
								return;
							} 
						}
						throw new MockScrapeException("No scenario with id='" + result+ "' was found.");
					} else {
						throw new MockScrapeException("No result returned from <script> block.");
					}
				} else {
					throw new MockScrapeException("Currently only lang='groovy' supported on <script> tag.");
				}
			} else {
				log.warn("No <script> block defined, defaulting to first scenario.");
				processScenario(mockSession.scenarios.get(0));
			}
		} else {
			throw new MockScrapeException("No scenarios defined in MockSession");
		}
	}
	
	protected void processScenario(MockScenario scenario) {
		for (MockDataSet mockdataset : scenario.datasets) {
			DataSet dataset = new DataSet();
			for (MockDataRecord mockRecord : mockdataset.datarecords) {
				log.debug(mockRecord.toString());
				DataRecord datarec = new DataRecord();
				for (MockDataRecordField field : mockRecord.fields) {
					datarec.put(field.name, field.value);
				}
				dataset.addDataRecord(datarec);
			}
			datasets.put(mockdataset.id, dataset);
		}
		if (scenario.variables != null) {
			for (MockVariable var : scenario.variables) {
				this.variables.put(var.name, var.value);
			}
		}
	}

	

	@Override
	protected DataSet getDataSet(String name) throws ScrapeException {
		return datasets.get(name);
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
	private Object runScript(String script, Object session, Map<String,String> cookiejar) {
		log.debug(String.format("Running Script {\n%s\n}", script));
		Binding binding = new Binding();
		binding.setVariable("cookiejar", cookiejar);
		binding.setVariable("session", session);
		GroovyShell shell = new GroovyShell(binding);

		Object result = shell.evaluate(script);
		log.debug("Script result: " + result);
		return result;
	}
	
}
