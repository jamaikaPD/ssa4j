package org.ssa4j.example.annotatted;

import java.io.File;

import org.ssa4j.ScrapeDataSet;
import org.ssa4j.ScrapeSession;
import org.ssa4j.ScrapeSessionCookie;
import org.ssa4j.ScrapeSessionCookies;
import org.ssa4j.ScrapeSessionManager;
import org.ssa4j.ScrapeSessionVariable;
import org.ssa4j.enterprise.EnterpriseScrapeScriptDeployer;
import org.ssa4j.mock.MockScrapeSessionManager;

@ScrapeSessionCookies({ @ScrapeSessionCookie("zenid") })
@ScrapeSession(name = "Shopping Site")
public class ShoppingSiteScrapingSession {

	protected String email;

	protected String password;
	
	protected String searchKeyWord;
	
	protected Integer page;

	@ScrapeDataSet("PRODUCTS")
	protected Product[] products;
	
	
	@ScrapeSessionVariable(name = "EMAIL_ADDRESS")
	public String getEmail() {
		return email;
	}

	@ScrapeSessionVariable(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	@ScrapeSessionVariable(name = "SEARCH")
	public String getSearchKeyWord() {
		return searchKeyWord;
	}

	@ScrapeSessionVariable(name = "PAGE")
	public Integer getPage() {
		return page;
	}
	
	

	public void setEmailAddress(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Product[] getProducts() {
		return products;
	}
	
	
	
	public static void main(String[] args) {
		try {
			EnterpriseScrapeScriptDeployer deployer = new EnterpriseScrapeScriptDeployer();
			deployer.deploy(new File("scraper/Shopping Site (Scraping Session).sss"));
			
			ShoppingSiteScrapingSession session = new ShoppingSiteScrapingSession();
			session.setEmailAddress("test@test.com");
			session.setPassword("testing");
			session.setSearchKeyWord("dvd");
			session.setPage(1);
			
			ScrapeSessionManager manager = new MockScrapeSessionManager();
			manager.scrape(session);
			
			for (Product product : session.getProducts()) {
				System.out.println("==============");
				System.out.println("Title: " + product.getTitle());
				System.out.println("Model: " + product.getModel());
				System.out.println("Shipping Weight: " + product.getWeight());
				System.out.println("Manufactured By: "
						+ product.getManufacturer());
				System.out.println("==============");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
