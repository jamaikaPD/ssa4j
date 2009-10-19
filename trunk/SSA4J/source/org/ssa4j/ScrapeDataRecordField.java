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
@Target(value={ElementType.METHOD, ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeDataRecordField {
	
	/**
	 * The name of the field as communicated by Screen-Scraper
	 * @return
	 */
	String name();
	
	/**
	 * A format string used to assist in the convertion of raw field
	 * values into the expected class types.
	 * @return
	 */
	String pattern() default "";
}
