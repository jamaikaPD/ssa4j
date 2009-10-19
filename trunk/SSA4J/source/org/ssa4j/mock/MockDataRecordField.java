package org.ssa4j.mock;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name="fieldType")
public  class MockDataRecordField {
	@XmlAttribute
	public String name;
	
	@XmlValue
	public String value; 
}
