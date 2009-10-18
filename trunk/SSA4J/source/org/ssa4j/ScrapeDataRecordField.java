package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method level annotation used to describe how a particular
 * field of a DataRecord maps to the class method.  This should
 * only be use with property structure "set" methods.
 * 
 * @author Rodney Aiglstorfer
 *
 */
@Documented
@Target(value=ElementType.METHOD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeDataRecordField {
	public enum FieldType {
		NUMBER,
		STRING,
		DATE,
		CUSTOM
	};
	
	/**
	 * The name of the field as communicated by Screen-Scraper
	 * @return
	 */
	String name();
	
	/**
	 * A hint for the ScrapeSessionManager to know how to process the 
	 * field value for a given DataRecord returned by Screen-Scraper
	 * @return
	 */
	FieldType type() default FieldType.STRING;
	
	/**
	 * A format string used to assist in the convertion of raw field
	 * values into the expected class types.
	 * @return
	 */
	String pattern() default "hh:mma d MMM yyyy";
	
	/**
	 * The key for the label that should be used to display this value
	 * @return
	 */
	String labelkey() default "";
}
