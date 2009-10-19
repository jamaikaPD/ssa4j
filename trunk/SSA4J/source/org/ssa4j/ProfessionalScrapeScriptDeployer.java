package org.ssa4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfessionalScrapeScriptDeployer extends ScrapeScriptDeployer {
	
	File importDir = null;
	
	/**
	 * Creates a ScreenScraper script deployer
	 * @param dir The install directory for the screen-scraper product
	 * @throws ScrapeException
	 */
	public ProfessionalScrapeScriptDeployer(File homeDir) throws ScrapeException {
		if (homeDir.exists()) {
			importDir = new File(homeDir, "import");
			if (importDir.exists() == false) {
				throw new ScrapeException("Screen-Scraper 'import' directory '" + importDir + "' not found.");
			}
		} else {
			throw new ScrapeException("Screen-Scraper install directory '" + homeDir + "' not found.");
		}
	}

	@Override
	protected void deploy(String name, byte[] data) throws IOException {
		FileOutputStream fout = new FileOutputStream(new File(importDir, name));
		
		fout.write(data);
		
		fout.flush();
		fout.close();
	}

}
