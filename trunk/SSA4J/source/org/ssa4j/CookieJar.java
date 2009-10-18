package org.ssa4j;


public interface CookieJar {
	public void setAttribute(String name, String value);
	public String getAttribute(String name);
	public String[] getAttributeNames();
}
