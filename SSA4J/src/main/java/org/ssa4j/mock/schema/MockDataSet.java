package org.ssa4j.mock.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * POJO that binds to the &lt;dataset/&gt; tags in a Mock session data file.
 * 
 * @author Rodney Aiglstorfer
 */
@XmlType(name="datasetType", namespace="http://schemas.mobuser.com/ssa4jmock")
public class MockDataSet {
	
	@XmlAttribute
	public String id;
	
	@XmlElement(name="datarecord")
	public List<MockDataRecord> datarecords = new ArrayList<MockDataRecord>();
	
	
}
