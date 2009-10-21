package org.ssa4j.example.annotatted;

import org.ssa4j.ScrapeDataSet;
import org.ssa4j.ScrapeException;
import org.ssa4j.ScrapeSession;
import org.ssa4j.ScrapeSessionCookie;
import org.ssa4j.ScrapeSessionCookies;
import org.ssa4j.ScrapeSessionManager;
import org.ssa4j.ScrapeSessionVariable;
import org.ssa4j.mock.MockScrapeSessionManager;

@ScrapeSessionCookies({ @ScrapeSessionCookie("zenid") })
@ScrapeSession(name = "Shopping Site")
public class ShoppingSiteScrapingSession {

	@ScrapeSessionVariable(name = "EMAIL_ADDRESS")
	protected String email;

	@ScrapeSessionVariable(name = "PASSWORD")
	protected String password;
	
	@ScrapeSessionVariable(name = "SEARCH")
	protected String searchKeyWord;
	
	@ScrapeSessionVariable(name = "PAGE")
	protected Integer page;

	@ScrapeDataSet("PRODUCTS")
	protected Product[] products;
	
	
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
		} catch (ScrapeException e) {
			e.printStackTrace();
		}
	}

}
