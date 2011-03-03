package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An embedded annotation used within the {@link ScrapeSessionErrors}
 * annotation to identify session variables that, if present, will
 * trigger as a {@link ScrapeSessionException} when the 
 * {@link ScrapeSessionManager#scrape(Object)} or 
 * {@link ScrapeSessionManager#scrape(Object, ScrapeSessionListener, java.util.Map)}
 * methods are invoked.
 * @author Rodney Aiglstorfer
 */
@Documented
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeSessionError {
	/**
	 * The name of the session variable that, if present, will trigger the {@link ScrapeSessionException}
	 * The message of the exception will be the value of the session variable.
	 * @return
	 */
	String name();
	
	/**
	 * An arbitrary code that is defined by the application.  It is used to test for a particular
	 * error.
	 * @see ScrapeSessionException#getErrors()
	 * @return
	 */
	int code();
}
