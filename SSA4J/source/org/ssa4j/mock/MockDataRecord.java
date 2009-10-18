package org.ssa4j.mock;

import java.util.Map;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

/**
 * POJO that binds to the &lt;field/&gt; tags in a Mock session data file.
 * 
 * @author Rodney Aiglstorfer
 *
 */
@Root(name="datarecord")
public class MockDataRecord extends MockObject {
	
	@ElementMap(entry="field", key="name", attribute=true, inline=true)
	public Map<String, String> map;
	
}
