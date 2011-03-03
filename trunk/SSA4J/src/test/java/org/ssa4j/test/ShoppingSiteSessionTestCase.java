package org.ssa4j.test;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssa4j.ScrapeException;
import org.ssa4j.ScrapeSessionManager;
import org.ssa4j.enterprise.EnterpriseScrapeScriptDeployer;
import org.ssa4j.enterprise.EnterpriseScrapeSessionManager;
import org.ssa4j.example.annotatted.Product;
import org.ssa4j.example.annotatted.ShoppingSiteScrapingSession;
import org.ssa4j.mock.MockScrapeSessionManager;
import org.ssa4j.professional.ProfessionalScrapeSessionManager;

import com.screenscraper.common.DataSet;
import com.screenscraper.scraper.RemoteScrapingSession;
import com.screenscraper.scraper.RemoteScrapingSessionException;

public class ShoppingSiteSessionTestCase extends TestCase {

	protected static Logger log = LoggerFactory.getLogger(ShoppingSiteSessionTestCase.class);
	
	@Before
	public void loadScreenScraperScript() throws ScrapeException, IOException {
		File sssFile = new File("scraper/Shopping Site (Scraping Session).sss");
		if (sssFile.exists() == false) {
			fail("File not found: " + sssFile.getAbsolutePath());
		}
		EnterpriseScrapeScriptDeployer deployer = new EnterpriseScrapeScriptDeployer();
		deployer.deploy(sssFile);
	}
	
	@Test
	public void testUsingLegacyAPI() {
		try {
			long startTime = System.currentTimeMillis();
			// Generate a RemoteScrapingSession object, which 
			// acts as the driver, or interface, to screen-scraper.
			// If you're running screen-scraper on a different computer 
			// than the one the Java file resides on you'll want to 
			// modify and use the top version instead of the bottom one.
			//RemoteScrapingSession remoteScrapingSession = new RemoteScrapingSession("Shopping Site", "192.168.0.5", 8778 );
			RemoteScrapingSession remoteScrapingSession = new RemoteScrapingSession("Shopping Site");
			remoteScrapingSession.setVariable( "PAGE", "1" );

			// Set the variables.
			// Remember that these two top variables correspond to the POST
			// parameters we use for the "Login" scrapeable file.
			remoteScrapingSession.setVariable( "EMAIL_ADDRESS", "test@test.com" );
			remoteScrapingSession.setVariable( "PASSWORD", "testing" );
			// screen-scraper will use this parameter to search the products.
			remoteScrapingSession.setVariable("SEARCH","dvd");
			// We start screen-scraper at page 1 of the search results.
			// Note that we could have also done this in an "initialize" 
			// script within screen-scraper, which is common.
			remoteScrapingSession.setVariable( "PAGE", "1" );

			// Tell the session to scrape. This method call might take
			// a little while since it will need to wait for screen-scraper
			// to fully extract the data before it returns.
			remoteScrapingSession.scrape();

			// Get the data set that was stored by screen-scraper in a
			// session variable. This data set corresponds to the "PRODUCTS"
			// extractor pattern found under the "Details page" scrapeable
			// file.
			DataSet products = ( DataSet )remoteScrapingSession.getVariable( "PRODUCTS" );

			// Be sure to disconnect from the server.
			remoteScrapingSession.disconnect();
			
			assertEquals("Number of products scraped", 18, products.getNumDataRecords());
			System.out.printf("Completed in %dms\n\n", System.currentTimeMillis()-startTime);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteScrapingSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testUsingLegacyWrapper() throws Exception {
		org.ssa4j.example.traditional.ShoppingSiteScrapingSession session = 
			new org.ssa4j.example.traditional.ShoppingSiteScrapingSession();
		session.setEmailAddress("test@test.com");
		session.setPassword("testing");
		session.setSearchKeyWord("dvd");
		session.setPage(1);
		
		session.scrape();
	}
	
	@Test
	public void testUsingRemoteScrapeSessionManager() throws Exception {
		long startTime = System.currentTimeMillis();
		// Create an instance of the annotated ScrapeSession POJO and
		// set the values for all the annotated ScrapeSessionVariables
		ShoppingSiteScrapingSession shoppingsession = new ShoppingSiteScrapingSession();
		shoppingsession.setEmailAddress("test@test.com");
		shoppingsession.setPassword("testing");
		shoppingsession.setSearchKeyWord("dvd");
		shoppingsession.setPage(1);
		
		// Create an instance of a ScrapeSessionManager
		// NOTE: by default, a RemoteScrapeSessionManager is used which is compatible
		// with both Profession and Enterprise editions of screen-scraper
		ScrapeSessionManager scraper = new ProfessionalScrapeSessionManager();
		
		Map<String, String> cookieJar = new HashMap<String, String>();
		// Tell the ScrapeSessionManager to scrape by passing in the annotated POJO
		scraper.scrape(shoppingsession, cookieJar);
		
		// Now iterate through the results with ease.  Notice that at this point you
		// are interacting with the POJOs.
		
		assertSession(shoppingsession, cookieJar, 18);
		System.out.printf("Completed in %dms\n\n", System.currentTimeMillis()-startTime);
	}
	
	@Test
	public void testUsingSoapScrapeSessionManager() throws Exception {
		long startTime = System.currentTimeMillis();
		// Create an instance of the annotated ScrapeSession POJO and
		// set the values for all the annotated ScrapeSessionVariables
		ShoppingSiteScrapingSession shoppingsession = new ShoppingSiteScrapingSession();
		shoppingsession.setEmailAddress("test@test.com");
		shoppingsession.setPassword("testing");
		shoppingsession.setSearchKeyWord("dvd");
		shoppingsession.setPage(1);
		
		// Create an instance of a ScrapeSessionManager
		// NOTE: by default, a RemoteScrapeSessionManager is used which is compatible
		// with both Profession and Enterprise editions of screen-scraper
		ScrapeSessionManager scraper = new EnterpriseScrapeSessionManager();
		
		Map<String, String> cookieJar = new HashMap<String, String>();
		// Tell the ScrapeSessionManager to scrape by passing in the annotated POJO
		scraper.scrape(shoppingsession, cookieJar);
		
		// Now iterate through the results with ease.  Notice that at this point you
		// are interacting with the POJOs.
		assertSession(shoppingsession, cookieJar,18);
		System.out.printf("Completed in %dms\n\n", System.currentTimeMillis()-startTime);
	}
	
	@Test
	public void testUsingMockScrapeSessionManager() throws Exception {
		long startTime = System.currentTimeMillis();
		// Create an instance of the annotated ScrapeSession POJO and
		// set the values for all the annotated ScrapeSessionVariables
		ShoppingSiteScrapingSession shoppingsession = new ShoppingSiteScrapingSession();
		shoppingsession.setEmailAddress("test@test.com");
		shoppingsession.setPassword("testing");
		shoppingsession.setSearchKeyWord("dvd");
		shoppingsession.setPage(1);
		
		// Create an instance of a ScrapeSessionManager
		// NOTE: by default, a RemoteScrapeSessionManager is used which is compatible
		// with both Profession and Enterprise editions of screen-scraper
		ScrapeSessionManager scraper = new MockScrapeSessionManager();
		
		Map<String, String> cookieJar = new HashMap<String, String>();
		// Tell the ScrapeSessionManager to scrape by passing in the annotated POJO
		scraper.scrape(shoppingsession, cookieJar);
		
		// Now iterate through the results with ease.  Notice that at this point you
		// are interacting with the POJOs.
		
		assertSession(shoppingsession, cookieJar, 6);
		System.out.printf("Completed in %dms\n\n", System.currentTimeMillis()-startTime);
	}
	
	@Test
	public void testUsingMockScrapeSessionManagerPage2() throws Exception {
		long startTime = System.currentTimeMillis();
		// Create an instance of the annotated ScrapeSession POJO and
		// set the values for all the annotated ScrapeSessionVariables
		ShoppingSiteScrapingSession shoppingsession = new ShoppingSiteScrapingSession();
		shoppingsession.setEmailAddress("test@test.com");
		shoppingsession.setPassword("testing");
		shoppingsession.setSearchKeyWord("dvd");
		shoppingsession.setPage(2);
		
		// Create an instance of a ScrapeSessionManager
		// NOTE: by default, a RemoteScrapeSessionManager is used which is compatible
		// with both Profession and Enterprise editions of screen-scraper
		ScrapeSessionManager scraper = new MockScrapeSessionManager();
		
		Map<String, String> cookieJar = new HashMap<String, String>();
		// Tell the ScrapeSessionManager to scrape by passing in the annotated POJO
		scraper.scrape(shoppingsession, cookieJar);
		
		// Now iterate through the results with ease.  Notice that at this point you
		// are interacting with the POJOs.
		
		assertEquals("Number of products scraped", 2, shoppingsession.getProducts().length);
		System.out.printf("Completed in %dms\n\n", System.currentTimeMillis()-startTime);
	}
	
	@Test
	public void testUsingMockScrapeSessionManagerPage3NoDataSet() throws Exception {
		long startTime = System.currentTimeMillis();
		// Create an instance of the annotated ScrapeSession POJO and
		// set the values for all the annotated ScrapeSessionVariables
		ShoppingSiteScrapingSession shoppingsession = new ShoppingSiteScrapingSession();
		shoppingsession.setEmailAddress("test@test.com");
		shoppingsession.setPassword("testing");
		shoppingsession.setSearchKeyWord("dvd");
		shoppingsession.setPage(3);
		
		// Create an instance of a ScrapeSessionManager
		// NOTE: by default, a RemoteScrapeSessionManager is used which is compatible
		// with both Profession and Enterprise editions of screen-scraper
		ScrapeSessionManager scraper = new MockScrapeSessionManager();
		
		Map<String, String> cookieJar = new HashMap<String, String>();
		// Tell the ScrapeSessionManager to scrape by passing in the annotated POJO
		scraper.scrape(shoppingsession, cookieJar);
		
		// Now iterate through the results with ease.  Notice that at this point you
		// are interacting with the POJOs.
		
		assertNull("products scraped", shoppingsession.getProducts());
		System.out.printf("Completed in %dms\n\n", System.currentTimeMillis()-startTime);
	}
	
	
	public void assertSession(ShoppingSiteScrapingSession session, Map<String,String> cookieJar, int numResults) {
		assertNotNull("zenid", cookieJar.get("zenid"));
		assertNotNull("session.getProducts()", session.getProducts());
		assertEquals("Number of products scraped", numResults, session.getProducts().length);
		for (Product product : session.getProducts()) {
			assertNotNull("Title", product.getTitle());
			assertNotNull("Model", product.getModel() );
			assertNotNull("Shipping Weight", product.getWeight() );
			assertNotNull("Manufactured By", product.getManufacturer() );
		}
	}
}
