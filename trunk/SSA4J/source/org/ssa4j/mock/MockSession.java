package org.ssa4j.mock;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO that binds to the &lt;mocksession/&gt; tags in a Mock session data file.
 * 
 * @author Rodney Aiglstorfer
 */
@XmlRootElement(name="mocksession")
public class MockSession extends MockObject {
	
	@XmlAttribute
	public String id;
	
	@XmlElement(name="scenario")
	public List<MockScenario> scenarios = new ArrayList<MockScenario>();
	
}
