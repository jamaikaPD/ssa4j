package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An embedded annotation that is used in conjunction with the
 * {@link ScrapeSessionCookies} annotation to identify a Session
 * variable that should be stored in the cookieJar and/or reflected
 * back if already present in the cookieJar.
 * 
 * @author Rodney Aiglstorfer
 */
@Documented
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeSessionCookie {
	/**
	 * The name of the session variable to store/read from a cookieJar.
	 * @return
	 */
	String value();
}
