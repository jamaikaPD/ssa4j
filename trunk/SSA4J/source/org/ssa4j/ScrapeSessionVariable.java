package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field level annotation used to identify a member variable as a 
 * Session variable to be passed into the scraping session.
 * 
 * @author Rodney Aiglstorfer
 *
 */
@Documented
@Target(value=ElementType.FIELD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeSessionVariable {
	enum BindType {
		Read,
		Write,
		ReadWrite
	}
	String name();
	String format() default "";
	BindType bindtype() default BindType.Write;
}
