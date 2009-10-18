package org.ssa4j.mock;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * POJO that binds to the &lt;mocksession/&gt; tags in a Mock session data file.
 * 
 * @author Rodney Aiglstorfer
 */
@Root(name="mocksession")
public class MockSession extends MockObject {
	
	@Attribute
	public String id;
	
	@ElementList(inline=true)
	public ArrayList<MockScenario> scenarios = new ArrayList<MockScenario>();
	
}
