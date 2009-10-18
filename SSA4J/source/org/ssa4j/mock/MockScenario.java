package org.ssa4j.mock;

import java.util.ArrayList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * POJO that binds to the &lt;scenario/&gt; tags in a Mock session data file.
 * 
 * @author Rodney Aiglstorfer
 *
 */
@Root(name="scenario")
public class MockScenario extends MockObject {
	
	@Element(name="test")
	public String testScript;
	
	@ElementList(name="dataset", inline=true, required=false)
	public ArrayList<MockDataSet> datasets = new ArrayList<MockDataSet>();
	
	@Element(name="variables", required=false)
	public MockVariables variables = new MockVariables();
}
