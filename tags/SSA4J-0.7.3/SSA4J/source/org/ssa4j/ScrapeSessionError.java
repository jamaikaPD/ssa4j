package org.ssa4j;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ScrapeSessionError {
	String name();
	int code();
}
