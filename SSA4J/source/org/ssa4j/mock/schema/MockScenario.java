package org.ssa4j.mock.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * POJO that binds to the &lt;scenario/&gt; tags in a Mock session data file.
 * 
 * @author Rodney Aiglstorfer
 *
 */
@XmlType(name="scenarioType", namespace="http://schemas.mobuser.com/ssa4jmock")
public class MockScenario {
	
	@XmlElement(name="test")
	public String testScript;
	
	@XmlElement(name="dataset", required=false)
	public List<MockDataSet> datasets = new ArrayList<MockDataSet>();
	
	@XmlElementWrapper(name="variables", required=false)
	@XmlElement(name="variable", required=false)
	public List<MockVariable> variables = new ArrayList<MockVariable>();
}
