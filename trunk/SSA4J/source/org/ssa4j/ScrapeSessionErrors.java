package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class level annotation that defines a list of possible session 
 * variables that, if present, will trigger a {@link ScrapeSessionException}.
 * @author Rodney Aiglstorfer
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeSessionErrors {
	/**
	 * A list of {@link ScrapeSessionError} annotations.  One for each
	 * session variable that may signal an exception has occured with 
	 * the scrape.
	 * @return
	 */
	public ScrapeSessionError[] value() default {};
}
