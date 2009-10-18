package org.ssa4j.mock;

import java.util.Map;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

@Root(name="variables")
public class MockVariables {
	@ElementMap(entry="variable", key="name", attribute=true, inline=true)
	public Map<String, String> map;
}
