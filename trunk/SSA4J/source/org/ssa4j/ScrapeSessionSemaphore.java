package org.ssa4j;

import java.util.Map;

public class ScrapeSessionSemaphore<T> implements ScrapeSessionListener<T> {
	
	private boolean running = true;
	private boolean errors = false;
	private boolean timeout = false;
	private T session = null;
	private Map<String, String> cookieJar = null;
	private Throwable cause;

	@Override
	public void onScrapeComplete(T session, Map<String, String> cookieJar) {
		this.session = session;
		this.cookieJar = cookieJar;
		notifyComplete();
	}

	@Override
	public void onScrapeError(T session, Map<String, String> cookieJar, Throwable t) {
		this.session = session;
		this.cookieJar = cookieJar;
		this.cause = t;
		this.errors = true;
		notifyComplete();
	}

	@Override
	public void onScrapeReady(T session, Map<String, String> cookieJar) {
		this.running = true;
		this.errors = false;
		this.timeout = false;
		this.session = null;
		this.cookieJar = null;
		this.cause = null;
	}

	@Override
	public void onScrapeTimeout(T session, Map<String, String> cookieJar) {
		this.session = session;
		this.cookieJar = cookieJar;
		this.errors = true;
		this.timeout = true;
		notifyComplete();
	}
	
	private synchronized void notifyComplete() {
		this.running = false;
		this.notifyAll();
	}
	
	public boolean isRunning() {
		return running;
	}

	public boolean hasErrors() {
		return errors;
	}

	public boolean didTimeout() {
		return timeout;
	}

	public T getSession() {
		return session;
	}

	public Map<String, String> getCookieJar() {
		return cookieJar;
	}

	public Throwable getCause() {
		return cause;
	}
	
	

}
