package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Field level annotation used to identify a member variable as a 
 * Session variable to read/write from/into a scraping session.
 * 
 * @author Rodney Aiglstorfer
 *
 */
@Documented
@Target(value={ElementType.FIELD, ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeSessionVariable {
	enum BindType {
		Read,
		Write
	}
	/**
	 * The name of the session variable
	 * @return
	 */
	String name();
	/**
	 * The format string to apply to the session variable when 
	 * binding.  This is either a format string like the one used
	 * by {@link String#format(String, Object...)} or, if the 
	 * annotated type is a {@link Date}, then its the format string 
	 * as defined by {@link SimpleDateFormat}
	 * @return
	 */
	String format() default "";
	BindType[] bindtype() default {BindType.Write};
}
