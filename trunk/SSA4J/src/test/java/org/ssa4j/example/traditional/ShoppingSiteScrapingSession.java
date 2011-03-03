package org.ssa4j.example.traditional;

import java.io.IOException;
import java.net.UnknownHostException;

import com.screenscraper.common.DataRecord;
import com.screenscraper.common.DataSet;
import com.screenscraper.scraper.RemoteScrapingSession;
import com.screenscraper.scraper.RemoteScrapingSessionException;

//This is a subclass of RemoteScrapingSession that abstracts the caller
//from having to know anything about the underlying RemoteScrapingSession
//and exposes a "typed" interface for setting session variables and 
//retrieving product content that was scraped from the site.
public class ShoppingSiteScrapingSession {

	RemoteScrapingSession session;

	// This is a simple constructor that hides the string literal
	// "Shopping Site"
	// from the caller. This is useful in the event that this string changes
	// later during development it won't impact anyone creating this class.
	public ShoppingSiteScrapingSession() throws UnknownHostException,
			RemoteScrapingSessionException, IOException {
		session = new RemoteScrapingSession("Shopping Site");
	}

	// Now we create a setter method for every session variable that we want
	// the caller to provide. For each, we call this.setVariable(..)
	// with the String literal required for each session variable.
	public void setEmailAddress(String emailAddress)
			throws RemoteScrapingSessionException {
		session.setVariable("EMAIL_ADDRESS", emailAddress);
	}

	public void setPassword(String password)
			throws RemoteScrapingSessionException {
		session.setVariable("PASSWORD", password);
	}

	public void setSearchKeyWord(String searchKeyWord)
			throws RemoteScrapingSessionException {
		session.setVariable("SEARCH", searchKeyWord);
	}

	public void setPage(int page) throws RemoteScrapingSessionException {
		session.setVariable("PAGE", Integer.toString(page));
	}

	// Because we don't want to expose the raw the DataSet to the caller, we
	// create
	// a method that converts the DataSet into an array of Product objects
	public Product[] getProducts() throws RemoteScrapingSessionException {
		DataSet dset = (DataSet) session.getVariable("PRODUCTS");
		Product[] products = new Product[dset.getNumDataRecords()];
		for (int i = 0; i < products.length; i++) {
			DataRecord rec = dset.getDataRecord(i);
			products[i] = new Product(rec);
		}
		return products;
	}
	
	public void scrape() throws RemoteScrapingSessionException {
		session.scrape();
	}

	public static void main(String[] args) {
		try {
			ShoppingSiteScrapingSession session = new ShoppingSiteScrapingSession();
			session.setEmailAddress("test@test.com");
			session.setPassword("testing");
			session.setSearchKeyWord("dvd");
			session.setPage(1);

			session.scrape();

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
