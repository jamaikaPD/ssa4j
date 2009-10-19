package org.ssa4j.mock.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.ssa4j.mock.MockObject;

/**
 * POJO that binds to the &lt;mocksession/&gt; tags in a Mock session data file.
 * 
 * @author Rodney Aiglstorfer
 */
@XmlRootElement(name="mocksession", namespace="http://schemas.mobuser.com/ssa4jmock")
@XmlType(name="mocksessionType", namespace="http://schemas.mobuser.com/ssa4jmock")
public class MockSession extends MockObject {
	
	@XmlAttribute
	public String id;
	
	@XmlElement(name="scenario")
	public List<MockScenario> scenarios = new ArrayList<MockScenario>();
	
}
