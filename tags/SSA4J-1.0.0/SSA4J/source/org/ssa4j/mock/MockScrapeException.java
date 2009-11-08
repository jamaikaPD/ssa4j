package org.ssa4j.mock;

import org.ssa4j.ScrapeException;

public class MockScrapeException extends ScrapeException {

	private static final long serialVersionUID = -2713851682911741458L;

	public MockScrapeException(String message) {
		super(message);
	}

	public MockScrapeException(Throwable cause) {
		super(cause);
	}

	public MockScrapeException(String message, Throwable cause) {
		super(message, cause);
	}

}
