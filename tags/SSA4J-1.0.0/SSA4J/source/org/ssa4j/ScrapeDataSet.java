package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field level annotation used to bind an array of {@link ScrapeDataRecord}
 * annotated objects to a DataSet construct returned by Screen-Scraper.  
 * 
 * @author Rodney Aiglstorfer
 *
 */
@Documented
@Target(value={ElementType.FIELD, ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeDataSet {
	/**
	 * The name of an Session Variable under which the dataset is
	 * stored in session.
	 */
	String value();
}
