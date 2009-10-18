package org.ssa4j.test;

import org.ssa4j.ScrapeDataSet;
import org.ssa4j.ScrapeSession;
import org.ssa4j.ScrapeSessionVariable;

@ScrapeSession(sessionId="Shopping Site")
public class ShoppingSiteSession {
	
	@ScrapeSessionVariable(name="EMAIL_ADDRESS")
	public String email;
	
	@ScrapeSessionVariable(name="PASSWORD")
	public String password;
	
	@ScrapeSessionVariable(name="SEARCH")
	public String search;
	
	@ScrapeSessionVariable(name="PAGE")
	public int page;
	
	@ScrapeDataSet(identifier="PRODUCTS")
	public Product[] products;
	
}