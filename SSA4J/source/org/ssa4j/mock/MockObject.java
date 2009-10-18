package org.ssa4j.mock;

import java.io.StringWriter;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Base class for all Mock objects.
 * 
 * @author Rodney Aiglstorfer
 *
 */
public class MockObject {
	public String toString() {
		Serializer serializer = new Persister();
		StringWriter result = new StringWriter();

		try {
			serializer.write(this, result);
		} catch (Exception e) {
			return super.toString();
		}
		
		return result.toString();

	}
}
