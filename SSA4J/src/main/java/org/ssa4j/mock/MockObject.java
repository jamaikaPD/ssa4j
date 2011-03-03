package org.ssa4j.mock;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;

import org.ssa4j.mock.schema.MockDataRecord;
import org.ssa4j.mock.schema.MockDataRecordField;
import org.ssa4j.mock.schema.MockDataSet;
import org.ssa4j.mock.schema.MockScenario;
import org.ssa4j.mock.schema.MockSession;
import org.ssa4j.mock.schema.MockVariable;

/**
 * Base class for all Mock objects.
 * 
 * @author Rodney Aiglstorfer
 *
 */
@XmlTransient
public class MockObject {
	
	private static Class<?>[] ctx = {
    	MockDataRecord.class,
    	MockDataRecordField.class,
    	MockDataSet.class,
    	MockScenario.class,
    	MockVariable.class,
    	MockSession.class
    };
	
	@SuppressWarnings("unchecked")
	public static <T> T toObject(Class<T> c, InputStream in) throws Exception {
		Unmarshaller um = JAXBContext.newInstance(ctx).createUnmarshaller();
		
		return (T) um.unmarshal(in);
	}
	
	public void toXML(OutputStream out) throws Exception {
		Marshaller m =	JAXBContext.newInstance(ctx).createMarshaller();
		
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		
		m.marshal( this, out );
	}
	
	public String toXML() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		this.toXML(out);
		return out.toString("UTF-8");
	}
	
	public String toString() {
		try {
			return this.toXML();
		} catch (Exception e) {
			e.printStackTrace();
			return super.toString();
		}
	}
}
