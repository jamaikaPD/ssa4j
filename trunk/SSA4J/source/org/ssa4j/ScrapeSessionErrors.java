package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeSessionErrors {
	public ScrapeSessionError[] value() default {};
}
