package org.ssa4j.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.ssa4j.EnterpriseScrapeScriptDeployer;
import org.ssa4j.ProfessionalScrapeScriptDeployer;
import org.ssa4j.ScrapeScriptDeployer;

import com.screenscraper.soapclient.SOAPInterface;
import com.screenscraper.soapclient.SOAPInterfaceService;
import com.screenscraper.soapclient.SOAPInterfaceServiceLocator;

public class ImportTestCase {

	@Test
	public void testUploadWithNativeSoapClient() throws Exception {
		SOAPInterfaceService service = new SOAPInterfaceServiceLocator();
		SOAPInterface soap = service.getSOAPInterface();
		
		soap.update(readFileAsString("scraper/Shopping Site (Scraping Session).sss"));
	}
	
	@Test
	public void testUploadWithProfessional() throws Exception {
		ScrapeScriptDeployer deployer = 
			new ProfessionalScrapeScriptDeployer(
					new File("/Applications/screen-scraper_enterprise"));
		
		deployer.deploy(new File("scraper"));
		
		deployer.deploy(new File("scraper/Shopping Site (Scraping Session).sss"));
	}
	
	@Test
	public void testUploadWithEnterprise() throws Exception {
		ScrapeScriptDeployer deployer = 
			new EnterpriseScrapeScriptDeployer();
		
		deployer.deploy(new File("scraper"));
		
		deployer.deploy(new File("scraper/Shopping Site (Scraping Session).sss"));
	}

	private static String readFileAsString(String filePath) throws IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

}
