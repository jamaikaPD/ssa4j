package org.ssa4j.professional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ssa4j.ScrapeConstants;
import org.ssa4j.ScrapeException;
import org.ssa4j.ScrapeScriptDeployer;

public class ProfessionalScrapeScriptDeployer extends ScrapeScriptDeployer {
	
	File importDir = null;
	
	public ProfessionalScrapeScriptDeployer() throws ScrapeException {
		String value = System.getProperty(ScrapeConstants.SSA4J_SS_HOME_DIR);
		if (value != null) {
			File homeDir = new File(value);
			setHomeDir(homeDir);
		} else {
			throw new ScrapeException("Screen-Scraper home directory not specified as System Property '" +
					ScrapeConstants.SSA4J_SS_HOME_DIR + "'");
		}
	}
	
	/**
	 * Creates a ScreenScraper script deployer
	 * @param dir The install directory for the screen-scraper product
	 * @throws ScrapeException
	 */
	public ProfessionalScrapeScriptDeployer(File homeDir) throws ScrapeException {
		setHomeDir(homeDir);
	}
	
	public void setHomeDir(File homeDir) throws ScrapeException {
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
