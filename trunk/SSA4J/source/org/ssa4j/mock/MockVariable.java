package org.ssa4j.mock;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name="variableType")
public class MockVariable {
	@XmlAttribute
	public String name;
	
	@XmlValue
	public String value; 
}
