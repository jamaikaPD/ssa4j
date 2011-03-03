package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class level annotation that defines a list of possible session 
 * variables that, if present, will read/written to/from the cookieJar.
 * 
 * @author Rodney Aiglstorfer
 */
@Documented
@Target(value=ElementType.TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeSessionCookies {
	public ScrapeSessionCookie[] value() default {};
}
