package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class level annontation that marks a class as being a target for
 * binding to a Scraping session.
 * 
 * @author Rodney Aiglstorfer
 *
 */
@Documented
@Target(value=ElementType.TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeSession {
	String name();
}
