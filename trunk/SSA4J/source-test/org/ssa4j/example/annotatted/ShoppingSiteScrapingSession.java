package org.ssa4j.example.annotatted;

import org.ssa4j.ProfessionalScrapeSessionManager;
import org.ssa4j.ScrapeDataSet;
import org.ssa4j.ScrapeException;
import org.ssa4j.ScrapeSession;
import org.ssa4j.ScrapeSessionCookie;
import org.ssa4j.ScrapeSessionCookies;
import org.ssa4j.ScrapeSessionManager;
import org.ssa4j.ScrapeSessionVariable;

@ScrapeSessionCookies(cookies = { @ScrapeSessionCookie("zenid") })
@ScrapeSession(name = "Shopping Site")
public class ShoppingSiteScrapingSession {

	@ScrapeSessionVariable(name = "EMAIL_ADDRESS")
	private String email;
	public void setEmailAddress(String email) {
		this.email = email;
	}

	@ScrapeSessionVariable(name = "PASSWORD")
	private String password;
	public void setPassword(String password) {
		this.password = password;
	}

	@ScrapeSessionVariable(name = "SEARCH")
	private String searchKeyWord;
	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}

	@ScrapeSessionVariable(name = "PAGE")
	private Integer page;
	public void setPage(int page) {
		this.page = page;
	}

	@ScrapeDataSet("PRODUCTS")
	private Product[] products;
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
			
			ScrapeSessionManager manager = new ProfessionalScrapeSessionManager();
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
