package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(value={})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeSessionError {
	String name();
	int code();
}
