package org.ssa4j.mock.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * POJO that binds to the &lt;field/&gt; tags in a Mock session data file.
 * 
 * @author Rodney Aiglstorfer
 *
 */
@XmlType(name="datarecordType", namespace="http://schemas.mobuser.com/ssa4jmock")
public class MockDataRecord {
	
	@XmlElement(name="field")
	public List<MockDataRecordField> fields = new ArrayList<MockDataRecordField>();
	
}
