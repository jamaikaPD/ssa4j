package org.ssa4j.mock;

import java.util.ArrayList;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * POJO that binds to the &lt;dataset/&gt; tags in a Mock session data file.
 * 
 * @author Rodney Aiglstorfer
 */
@Root(name="dataset")
public class MockDataSet extends MockObject {
	
	@Attribute
	public String id;
	
	@ElementList(name="datarecord", inline=true)
	public ArrayList<MockDataRecord> datarecords = new ArrayList<MockDataRecord>();
	
	
}
