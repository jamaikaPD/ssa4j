package org.ssa4j.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssa4j.RemoteScrapeSessionManager;
import org.ssa4j.ScrapeSessionManager;
import org.ssa4j.SoapScrapeSessionManager;
import org.ssa4j.mock.MockScrapeSessionManager;

import com.screenscraper.common.DataRecord;
import com.screenscraper.common.DataSet;
import com.screenscraper.scraper.RemoteScrapingSession;

import junit.framework.TestCase;

public class ShoppingSiteSessionTestCase extends TestCase {

	protected static Logger log = LoggerFactory.getLogger(ShoppingSiteSessionTestCase.class);
	
	@Test
	public void testUsingLegacyAPI() throws Exception {
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

		// Iterate through each of the data records screen-scraper
		// extracted, outputting each of them to the browser.
		for( int i = 0; i < products.getNumDataRecords(); i++ )
		{
			DataRecord product = products.getDataRecord( i );
			log.debug( "=======================================" );
			log.debug( "Product #" + i );
			log.debug( "Title: " + product.get( "TITLE" ) );
			log.debug( "Model: " + product.get( "MODEL" ) );
			log.debug( "Shipping Weight: " + product.get( "SHIPPING_WEIGHT" ) );
			log.debug( "Manufactured By: " + product.get( "MANUFACTURED_BY" ) );
			log.debug( "=======================================" );
		}

		// Be sure to disconnect from the server.
		remoteScrapingSession.disconnect();
		
		assertEquals("Number of products scraped", 18, products.getNumDataRecords());
		System.out.printf("Completed in %dms\n\n", System.currentTimeMillis()-startTime);
	}
	
	@Test
	public void testUsingRemoteScrapeSessionManager() throws Exception {
		long startTime = System.currentTimeMillis();
		// Create an instance of the annotated ScrapeSession POJO and
		// set the values for all the annotated ScrapeSessionVariables
		ShoppingSiteSession shoppingsession = new ShoppingSiteSession();
		shoppingsession.email = "test@test.com";
		shoppingsession.password = "testing";
		shoppingsession.search = "dvd";
		shoppingsession.page = 1;
		
		// Create an instance of a ScrapeSessionManager
		// NOTE: by default, a RemoteScrapeSessionManager is used which is compatible
		// with both Profession and Enterprise editions of screen-scraper
		ScrapeSessionManager scraper = new RemoteScrapeSessionManager();
		
		// Tell the ScrapeSessionManager to scrape by passing in the annotated POJO
		scraper.scrape(shoppingsession);
		
		// Now iterate through the results with ease.  Notice that at this point you
		// are interacting with the POJOs.
		for (Product product : shoppingsession.products) {
			log.debug( "=======================================" );
			log.debug( "Title: " + product.title );
			log.debug( "Model: " + product.model );
			log.debug( "Shipping Weight: " + product.weight );
			log.debug( "Manufactured By: " + product.manufacturer );
			log.debug( "=======================================" );
		}
		assertEquals("Number of products scraped", 18, shoppingsession.products.length);
		System.out.printf("Completed in %dms\n\n", System.currentTimeMillis()-startTime);
	}
	
	@Test
	public void testUsingSoapScrapeSessionManager() throws Exception {
		long startTime = System.currentTimeMillis();
		// Create an instance of the annotated ScrapeSession POJO and
		// set the values for all the annotated ScrapeSessionVariables
		ShoppingSiteSession shoppingsession = new ShoppingSiteSession();
		shoppingsession.email = "test@test.com";
		shoppingsession.password = "testing";
		shoppingsession.search = "dvd";
		shoppingsession.page = 1;
		
		// Create an instance of a ScrapeSessionManager
		// NOTE: by default, a RemoteScrapeSessionManager is used which is compatible
		// with both Profession and Enterprise editions of screen-scraper
		ScrapeSessionManager scraper = new SoapScrapeSessionManager();
		
		// Tell the ScrapeSessionManager to scrape by passing in the annotated POJO
		scraper.scrape(shoppingsession);
		
		// Now iterate through the results with ease.  Notice that at this point you
		// are interacting with the POJOs.
		for (Product product : shoppingsession.products) {
			log.debug( "=======================================" );
			log.debug( "Title: " + product.title );
			log.debug( "Model: " + product.model );
			log.debug( "Shipping Weight: " + product.weight );
			log.debug( "Manufactured By: " + product.manufacturer );
			log.debug( "=======================================" );
		}
		assertEquals("Number of products scraped", 18, shoppingsession.products.length);
		System.out.printf("Completed in %dms\n\n", System.currentTimeMillis()-startTime);
	}
	
	@Test
	public void testUsingMockScrapeSessionManager() throws Exception {
		long startTime = System.currentTimeMillis();
		// Create an instance of the annotated ScrapeSession POJO and
		// set the values for all the annotated ScrapeSessionVariables
		ShoppingSiteSession shoppingsession = new ShoppingSiteSession();
		shoppingsession.email = "test@test.com";
		shoppingsession.password = "testing";
		shoppingsession.search = "dvd";
		shoppingsession.page = 1;
		
		// Create an instance of a ScrapeSessionManager
		// NOTE: by default, a RemoteScrapeSessionManager is used which is compatible
		// with both Profession and Enterprise editions of screen-scraper
		ScrapeSessionManager scraper = new MockScrapeSessionManager();
		
		// Tell the ScrapeSessionManager to scrape by passing in the annotated POJO
		scraper.scrape(shoppingsession);
		
		// Now iterate through the results with ease.  Notice that at this point you
		// are interacting with the POJOs.
		for (Product product : shoppingsession.products) {
			log.debug( "=======================================" );
			log.debug( "Title: " + product.title );
			log.debug( "Model: " + product.model );
			log.debug( "Shipping Weight: " + product.weight );
			log.debug( "Manufactured By: " + product.manufacturer );
			log.debug( "=======================================" );
		}
		
		assertEquals("Number of products scraped", 18, shoppingsession.products.length);
		System.out.printf("Completed in %dms\n\n", System.currentTimeMillis()-startTime);
	}
}
