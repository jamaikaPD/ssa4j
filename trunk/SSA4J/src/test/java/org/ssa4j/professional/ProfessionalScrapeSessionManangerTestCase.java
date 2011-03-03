package org.ssa4j.professional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.junit.Test;
import org.ssa4j.ScrapeSessionListener;
import org.ssa4j.ScrapeSessionManager;
import org.ssa4j.example.annotatted.Product;
import org.ssa4j.example.annotatted.ShoppingSiteScrapingSession;

public class ProfessionalScrapeSessionManangerTestCase extends TestCase {

	ScrapeSessionManager scraper = new ProfessionalScrapeSessionManager();
	
	ScrapeSessionListener<ShoppingSiteScrapingSession> listener = new ScrapeSessionListener<ShoppingSiteScrapingSession>() {

		public void onScrapeComplete(ShoppingSiteScrapingSession session, Map<String, String> cookieJar) {
			assertShoppingSession(session, cookieJar);
		}

		public void onScrapeError(ShoppingSiteScrapingSession session, Map<String, String> cookieJar, Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}

		public void onScrapeReady(ShoppingSiteScrapingSession session, Map<String, String> cookieJar) {
			/* Ignore */
		}

		public void onScrapeTimeout(ShoppingSiteScrapingSession session, Map<String, String> cookieJar) {
			fail("ScrapeSession timed out");
		}
		
	};
	
	public ShoppingSiteScrapingSession createShoppingSiteScrapingSession(int page) throws Exception {
		// Create an instance of the annotated ScrapeSession POJO and
		// set the values for all the annotated ScrapeSessionVariables
		ShoppingSiteScrapingSession shoppingsession = new ShoppingSiteScrapingSession();
		shoppingsession.setEmailAddress("test@test.com");
		shoppingsession.setPassword("testing");
		shoppingsession.setSearchKeyWord("dvd");
		shoppingsession.setPage(page);
		
		return shoppingsession;
	}
	

	@Test
	public void testThreadLocalState() throws Exception {
		scraper.scrape(createShoppingSiteScrapingSession(1), new HashMap<String, String>(), listener);
		scraper.scrape(createShoppingSiteScrapingSession(2), new HashMap<String, String>(), listener);
		scraper.scrape(createShoppingSiteScrapingSession(3), new HashMap<String, String>(), listener);
		
		System.out.println("Waiting ...");
		ScrapeSessionManager.shutdown(30, TimeUnit.SECONDS);
		System.out.println("Done.");
	}
	
	public void assertShoppingSession(ShoppingSiteScrapingSession session, Map<String,String> cookieJar) {
		assertNotNull("page[" + session.getPage() + "] session.getProducts()", session.getProducts());
		assertTrue("page[" + session.getPage() + "] Number of products scraped greater than zero", session.getProducts().length > 0);
		for (Product product : session.getProducts()) {
			assertNotNull("page[" + session.getPage() + "] Title", product.getTitle());
			assertNotNull("page[" + session.getPage() + "] Model", product.getModel() );
			assertNotNull("page[" + session.getPage() + "] Shipping Weight", product.getWeight() );
			assertNotNull("page[" + session.getPage() + "] Manufactured By", product.getManufacturer() );
		}
		assertNotNull("page[" + session.getPage() + "] zenid", cookieJar.get("zenid"));
	}
}
