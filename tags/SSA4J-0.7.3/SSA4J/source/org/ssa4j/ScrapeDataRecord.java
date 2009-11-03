package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class level annotation used to mark a class as a target for
 * data binding to a Screen-Scraper DataRecord class.
 * 
 * @author Rodney Aiglstorfer
 *
 */
@Documented
@Target(value=ElementType.TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeDataRecord {}
